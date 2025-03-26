package fr.ul.miashs.jase;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.model.Processus;
import fr.ul.miashs.jase.ordonnanceur.*;


import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: java -cp target/classes jase.Main <config-file> <data-file>");
            return;
        }

        String configPath = args[0];
        String dataPath = args[1];

        Config config = new Config(configPath);
        List<Processus> tousLesProcessus = Processus.chargerDepuisFichier(dataPath);
        List<Processus> filePret = new ArrayList<>();
        List<Processus> enCours = new ArrayList<>();
        List<Processus> termines = new ArrayList<>();

        Ordonnanceur ordonnanceur;
        switch (config.getOrdonnancement()) {
            case "FIFO":
                ordonnanceur = new OrdonnanceurFIFO();
                break;
            case "tourniquet":
                ordonnanceur = new OrdonnanceurTourniquet(config.getQuantum());
                break;
            case "par-priorités":
                ordonnanceur = new OrdonnanceurPriorite();
                break;
            default:
                throw new IllegalArgumentException("Algorithme non supporté: " + config.getOrdonnancement());
        }

        int tempsActuel = 0;
        int quantum = config.getQuantum();
        int interruption = config.getInterruptionHorloge();
        Processus actif = null;
        int tempsRestant = 0;

        StringBuilder trace = new StringBuilder();

        while (tempsActuel < config.getTempsSimulation()) {
            for (Iterator<Processus> it = tousLesProcessus.iterator(); it.hasNext(); ) {
                Processus p = it.next();
                if (p.arrivee <= tempsActuel) {
                    ordonnanceur.ajouter(p);
                    it.remove();
                }
            }

            if (actif == null || tempsRestant == 0) {
                if (actif != null && actif.estTermine()) {
                    actif.tempsFin = tempsActuel;
                    termines.add(actif);
                } else if (actif != null) {
                    ordonnanceur.ajouter(actif);
                }
                actif = ordonnanceur.suivant();
                if (actif != null && actif.tempsDebutExecution == -1)
                    actif.tempsDebutExecution = tempsActuel;
                tempsRestant = quantum;
            }

            if (actif != null) {
                trace.append("[" + tempsActuel + "] P" + actif.id + "\n");
                actif.executer(interruption);
                tempsRestant -= interruption;
            } else {
                trace.append("[" + tempsActuel + "] IDLE\n");
            }

            tempsActuel += interruption;
        }

        // Dernier traitement
        if (actif != null && actif.estTermine()) {
            actif.tempsFin = tempsActuel;
            termines.add(actif);
        }

        double delaiRotation = termines.stream().mapToDouble(p -> p.tempsFin - p.arrivee).average().orElse(0);
        double reactivite = termines.stream().mapToDouble(p -> p.tempsDebutExecution - p.arrivee).average().orElse(0);

        System.out.println("\n--- TRACE D'EXECUTION ---");
        System.out.println(trace);
        System.out.println("--- STATISTIQUES ---");
        System.out.println("Capacité de traitement : " + termines.size());
        System.out.printf("Délai de rotation moyen : %.2f ms\n", delaiRotation);
        System.out.printf("Réactivité moyenne : %.2f ms\n", reactivite);
    }
}