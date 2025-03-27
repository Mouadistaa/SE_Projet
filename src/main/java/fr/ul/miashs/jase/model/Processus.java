package fr.ul.miashs.jase.model;

import java.io.*;
import java.util.*;

public class Processus {

    public enum EtatProcess {
        PRET,
        EN_COURS,
        BLOQUE,
        TERMINE
    }

    public int id;
    public int arrivee;      // moment d'arrivée
    public int priorite;     // 1..5
    public int tempsDebutExecution = -1;
    public int tempsFin = -1;

    public EtatProcess etat = EtatProcess.PRET;

    public List<Evenement> evenements = new ArrayList<>();

    /**
     * Chargement depuis un fichier de programmes.
     * Format: ID/arrivee/priorite/{CALCUL(50),ECRITURE(10)...}
     */
    public static List<Processus> chargerDepuisFichier(String fichier) throws IOException {
        List<Processus> resultat = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                ligne = ligne.strip();
                if (ligne.isEmpty() || ligne.startsWith("#")) {
                    continue; // ignorer lignes vides / commentaires
                }
                String[] parts = ligne.split("/", 4);
                if (parts.length < 4) {
                    System.err.println("Ligne invalide (pas assez de champs) : " + ligne);
                    continue;
                }

                Processus p = new Processus();
                p.id       = Integer.parseInt(parts[0].trim());
                p.arrivee  = Integer.parseInt(parts[1].trim());
                p.priorite = Integer.parseInt(parts[2].trim());

                String evts = parts[3].replaceAll("[{}]", "");
                String[] listeEvt = evts.split(",");
                for (String e : listeEvt) {
                    e = e.strip();
                    if (!e.contains("(")) {
                        System.err.println("Evénement invalide : " + e);
                        continue;
                    }
                    String typeStr = e.substring(0, e.indexOf('(')).trim();
                    String paramStr= e.substring(e.indexOf('(')+1, e.indexOf(')')).trim();
                    int param = Integer.parseInt(paramStr);

                    Evenement.Type ttype;
                    switch (typeStr) {
                        case "CALCUL":   ttype = Evenement.Type.CALCUL;   break;
                        case "ECRITURE": ttype = Evenement.Type.ECRITURE; break;
                        case "LECTURE":  ttype = Evenement.Type.LECTURE;  break;
                        case "DORMIR":   ttype = Evenement.Type.DORMIR;   break;
                        case "FIN":      ttype = Evenement.Type.FIN;      break;
                        default:
                            ttype = Evenement.Type.CALCUL; // fallback
                            System.err.println("Type inconnu: " + typeStr);
                    }
                    p.evenements.add(new Evenement(ttype, param));
                }
                resultat.add(p);
            }
        }
        return resultat;
    }

    /**
     * Exécuter le processus pendant 'duree' ms.
     * Ici, on gère principalement CALCUL(...) - on décrémente le 1er événement.
     * A étendre pour ECRITURE, LECTURE, etc.
     */
    public void executer(int duree) {
        if (!evenements.isEmpty()) {
            Evenement evt = evenements.get(0);
            switch (evt.type) {
                case CALCUL:
                    evt.param -= duree;
                    if (evt.param <= 0) {
                        evenements.remove(0);
                    }
                    break;
                case ECRITURE:
                case LECTURE:
                case DORMIR:
                case FIN:
                    // Pour l'instant, on ne gère pas encore le blocage/E/S,
                    // on peut juste consommer 'duree' pour dire qu'on avance
                    // virtuellement.
                    evt.param -= duree;
                    if (evt.param <= 0) {
                        evenements.remove(0);
                    }
                    break;
            }
        }
    }

    /** Indique si le processus a épuisé tous ses événements */
    public boolean estTermine() {
        return evenements.isEmpty();
    }
}
