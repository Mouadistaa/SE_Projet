package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;

/**
 * Implémentation ultra-simplifiée de l'algorithme Optimal.
 * En réalité, on a besoin de connaître la future séquence de références,
 * mais ici on fait un code minimal pour éviter l'erreur de compilation.
 */
public class RemplacementOptimal implements RemplacementStrategy {

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {
        // En théorie, on cherche la page dont la prochaine utilisation est la plus lointaine
        // Ici, on se contente d'un fallback minimal : prendre un cadre libre ou cadre 0.

        int cadreLibre = tablePages.trouverCadreLibre();
        if (cadreLibre >= 0) {
            chargerDansCadre(tablePages, cadreLibre, p, numeroPage, tempsCourant);
        } else {
            // Remplacer le cadre 0 (ou un cadre choisi au hasard)
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
