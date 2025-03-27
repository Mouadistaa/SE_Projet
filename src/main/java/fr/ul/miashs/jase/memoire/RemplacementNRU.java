package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;

/**
 * Implémentation (très) simplifiée de l'algorithme
 * "Non Récemment Utilisée" (NRU).
 * Ici, on fait un code minimal pour éviter l'erreur "Cannot resolve symbol".
 */
public class RemplacementNRU implements RemplacementStrategy {

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {
        // Exemple minimal : on va simplement se comporter comme FIFO
        // ou choisir le cadre 0, juste pour que ça compile.
        // Dans un vrai NRU, on classe les pages selon bits R/M etc.

        // Chercher un cadre libre
        int cadreLibre = tablePages.trouverCadreLibre();
        if (cadreLibre >= 0) {
            chargerDansCadre(tablePages, cadreLibre, p, numeroPage, tempsCourant);
        } else {
            // Pas de cadre libre, on remplace le cadre 0 (c'est un fallback simpliste)
            chargerDansCadre(tablePages, 0, p, numeroPage, tempsCourant);
        }
    }

    private void chargerDansCadre(TablePages tablePages, int indexCadre,
                                  Processus p, int numeroPage, int tempsCourant) {
        TablePages.Cadre c = tablePages.getCadre(indexCadre);
        c.occupe = true;
        c.proprietaire = p;
        c.numeroPage = numeroPage;
        c.derniereUtilisation = tempsCourant;
        c.bitReference = true;
        c.bitModifie = false;
    }
}
