package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;
import java.util.LinkedList;

public class RemplacementSecondeChance implements RemplacementStrategy {

    // On simule un "cercle" de cadres
    private final LinkedList<Integer> file = new LinkedList<>();
    private int pointeur = 0;

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {
        // Chercher d'abord un cadre libre
        int libre = tablePages.trouverCadreLibre();
        if (libre >= 0) {
            charger(tablePages, libre, p, numeroPage, tempsCourant);
            file.add(libre);
        } else {
            // Boucle pour trouver une page dont bitReference = false
            boolean remplace = false;
            while (!remplace) {
                if (pointeur >= file.size()) {
                    pointeur = 0;
                }
                int indexCadre = file.get(pointeur);
                TablePages.Cadre c = tablePages.getCadre(indexCadre);
                if (c.bitReference) {
                    // Donne une seconde chance
                    c.bitReference = false;
                    pointeur++;
                } else {
                    // On remplace ce cadre
                    charger(tablePages, indexCadre, p, numeroPage, tempsCourant);
                    // On reste sur ce pointeur
                    remplace = true;
                }
            }
        }
    }

    private void charger(TablePages tablePages, int indexCadre, Processus p, int numeroPage, int tempsCourant) {
        TablePages.Cadre c = tablePages.getCadre(indexCadre);
        c.occupe = true;
        c.proprietaire = p;
        c.numeroPage = numeroPage;
        c.derniereUtilisation = tempsCourant;
        c.bitReference = true;
        c.bitModifie = false;
    }
}
