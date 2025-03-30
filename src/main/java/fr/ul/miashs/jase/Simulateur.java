package fr.ul.miashs.jase;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.disque.GestionDisque;
import fr.ul.miashs.jase.memoire.GestionMemoire;
import fr.ul.miashs.jase.model.Evenement;
import fr.ul.miashs.jase.model.Processus;
import fr.ul.miashs.jase.ordonnanceur.Ordonnanceur;

import java.util.*;


public class Simulateur {

    private final Config config;
    private final Ordonnanceur ordonnanceurCPU;
    private final GestionMemoire gestionMemoire;
    private final GestionDisque gestionDisque;

    private final List<Processus> tousLesProcessus;
    private final List<Processus> termines = new ArrayList<>();

    private final Queue<Processus> fileBloquesMemoire = new LinkedList<>();
    private final Queue<Processus> fileBloquesDisque  = new LinkedList<>();

    private final StringBuilder trace = new StringBuilder();

    public Simulateur(Config config,
                      Ordonnanceur ordonnanceurCPU,
                      List<Processus> listeProcess) {
        this.config = config;
        this.ordonnanceurCPU = ordonnanceurCPU;
        this.gestionMemoire = new GestionMemoire(config);
        this.gestionDisque  = new GestionDisque(config);
        this.tousLesProcessus = new ArrayList<>(listeProcess);
    }

    public void run() {
        int tempsActuel  = 0;
        int interruption = config.getInterruptionHorloge();
        int quantum      = config.getQuantum();

        Processus actif  = null;
        int tempsRestantQuantum = 0;

        while (tempsActuel < config.getTempsSimulation()) {
            for (Iterator<Processus> it = tousLesProcessus.iterator(); it.hasNext();) {
                Processus p = it.next();
                if (p.arrivee <= tempsActuel) {
                    p.etat = Processus.EtatProcess.PRET;
                    ordonnanceurCPU.ajouter(p);
                    it.remove();
                }
            }

            if (actif == null || tempsRestantQuantum <= 0) {
                if (actif != null) {
                    if (actif.estTermine()) {
                        actif.tempsFin = tempsActuel;
                        actif.etat = Processus.EtatProcess.TERMINE;
                        termines.add(actif);
                    } else {
                        ordonnanceurCPU.ajouter(actif);
                        actif.etat = Processus.EtatProcess.PRET;
                    }
                }
                actif = ordonnanceurCPU.suivant();
                if (actif != null && !actif.estTermine()) {
                    if (actif.tempsDebutExecution < 0) {
                        actif.tempsDebutExecution = tempsActuel;
                    }
                    actif.etat = Processus.EtatProcess.EN_COURS;
                    tempsRestantQuantum = quantum;
                }
            }

            if (actif != null && !actif.estTermine() && actif.etat == Processus.EtatProcess.EN_COURS) {
                trace.append(String.format("[%d ms] CPU: P%d\n", tempsActuel, actif.id));
                Evenement evt = actif.evenements.isEmpty() ? null : actif.evenements.get(0);
                if (evt != null) {
                    switch(evt.type) {
                        case CALCUL:
                            actif.executer(interruption);
                            break;

                        case LECTURE:
                            int page = evt.param;
                            int tempsChargement = gestionMemoire.gererLecturePage(actif, page, tempsActuel);
                            if (tempsChargement > 0) {
                                bloquerProcessus(actif, tempsChargement, "memoire");
                                actif = null;
                            }
                            if (actif != null) actif.evenements.remove(0);
                            break;

                        case ECRITURE:
                            int piste = evt.param;
                            gestionDisque.ajouterRequete(actif, piste);
                            int tempsEcriture = config.getTempsEcritureDisque();
                            bloquerProcessus(actif, tempsEcriture, "disque");
                            actif.evenements.remove(0);
                            actif = null;
                            break;

                        case DORMIR:
                            bloquerProcessus(actif, evt.param, "memoire");
                            actif.evenements.remove(0);
                            actif = null;
                            break;

                        case FIN:
                            actif.evenements.remove(0);
                            if (actif.estTermine()) {
                                actif.tempsFin = tempsActuel;
                                actif.etat = Processus.EtatProcess.TERMINE;
                                termines.add(actif);
                                actif = null;
                            }
                            break;
                    }
                }
                if (actif != null) tempsRestantQuantum -= interruption;
            } else {
                trace.append(String.format("[%d ms] IDLE\n", tempsActuel));
            }

            gestionDisque.traiterRequete();
            majProcessusBloques(tempsActuel);
            tempsActuel += interruption;
        }

        if (actif != null && actif.estTermine() && !termines.contains(actif)) {
            actif.tempsFin = tempsActuel;
            actif.etat = Processus.EtatProcess.TERMINE;
            termines.add(actif);
        }
    }

    private void bloquerProcessus(Processus p, int duree, String typeBlocage) {
        p.etat = Processus.EtatProcess.BLOQUE;
        p.finBlocage = duree;
        switch(typeBlocage) {
            case "memoire":
            case "sleep":
                fileBloquesMemoire.offer(p);
                break;
            case "disque":
                fileBloquesDisque.offer(p);
                break;
        }
    }

    private void majProcessusBloques(int tempsActuel) {
        for (Processus p : fileBloquesMemoire) {
            p.finBlocage -= config.getInterruptionHorloge();
        }
        Iterator<Processus> itMem = fileBloquesMemoire.iterator();
        while(itMem.hasNext()) {
            Processus p = itMem.next();
            if (p.finBlocage <= 0) {
                p.etat = Processus.EtatProcess.PRET;
                ordonnanceurCPU.ajouter(p);
                itMem.remove();
            }
        }

        for (Processus p : fileBloquesDisque) {
            p.finBlocage -= config.getInterruptionHorloge();
        }
        Iterator<Processus> itDisque = fileBloquesDisque.iterator();
        while(itDisque.hasNext()) {
            Processus p = itDisque.next();
            if (p.finBlocage <= 0) {
                p.etat = Processus.EtatProcess.PRET;
                ordonnanceurCPU.ajouter(p);
                itDisque.remove();
            }
        }
    }

    public void afficherResultats() {
        System.out.println("\n--- TRACE D'EXECUTION ---");
        System.out.println(trace);

        double delaiRotation = termines.stream()
                .mapToDouble(p -> p.tempsFin - p.arrivee)
                .average()
                .orElse(0.0);

        double reactivite = termines.stream()
                .mapToDouble(p -> p.tempsDebutExecution - p.arrivee)
                .average()
                .orElse(0.0);

        System.out.println("--- STATISTIQUES ---");
        System.out.println("Nb processus terminés : " + termines.size());
        System.out.printf("Délai de rotation moyen : %.2f ms\n", delaiRotation);
        System.out.printf("Réactivité moyenne : %.2f ms\n", reactivite);

        System.out.println("--- DÉTAIL PAR PROCESSUS ---");
        for (Processus p : termines) {
            System.out.println("P" + p.id +
                    " | arrivée=" + p.arrivee +
                    " | début=" + p.tempsDebutExecution +
                    " | fin=" + p.tempsFin +
                    " | rotation=" + (p.tempsFin - p.arrivee));
        }
    }
}
