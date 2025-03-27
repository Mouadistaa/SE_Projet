package fr.ul.miashs.jase.disque;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.model.Processus;
import java.util.ArrayList;
import java.util.List;

/**
 * Gère les requêtes disque et l'ordonnancement du bras.
 */
public class GestionDisque {
    private int positionBras;
    private String directionInitiale; // "droite" ou "gauche"
    private final OrdonnanceurDisque algoDisque;

    private final List<RequeteDisque> requetes = new ArrayList<>();
    private int distanceTotale = 0;

    public GestionDisque(Config config) {
        this.positionBras = config.getDisquePositionInitiale();
        this.directionInitiale = config.getDisqueDirectionInitiale();

        // Choisir l'algo
        switch(config.getDisqueOrdonnancement()) {
            case "FIFO":
                this.algoDisque = new DisqueFIFO();
                break;
            case "ascenseur":
                this.algoDisque = new DisqueAscenseur();
                break;
            case "plus-proche":
                this.algoDisque = new DisquePlusProche();
                break;
            default:
                this.algoDisque = new DisqueFIFO();
        }
    }

    /**
     * Ajouter une requête d'écriture (ou lecture) sur le disque
     */
    public void ajouterRequete(Processus p, int piste) {
        RequeteDisque rq = new RequeteDisque(p, piste);
        requetes.add(rq);
    }

    /**
     * Appelée pour traiter une requête (ou plusieurs) selon l'algo.
     * Simule le déplacement du bras et renvoie le "temps" ou
     * distance qu'il faut.
     */
    public int traiterRequete() {
        // L'algorithme renvoie la requête suivante
        RequeteDisque rq = algoDisque.choisirRequete(requetes, positionBras, directionInitiale);
        if (rq == null) {
            return 0;
        }
        // Calculer la distance
        int distance = Math.abs(rq.piste - positionBras);
        distanceTotale += distance;
        // Déplacer le bras
        positionBras = rq.piste;

        // Retirer la requête de la liste
        requetes.remove(rq);

        // On peut imaginer un temps = distance + temps-ecriture-disque
        // Pour simplifier, on renvoie juste la distance
        return distance;
    }

    public int getDistanceTotale() {
        return distanceTotale;
    }
}
