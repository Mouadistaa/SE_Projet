package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;


public class StrategieRemplacementOptimal implements StrategieRemplacement {

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {

        int cadreLibre = tablePages.trouverCadreLibre();
        if (cadreLibre >= 0) {
            chargerDansCadre(tablePages, cadreLibre, p, numeroPage, tempsCourant);
        } else {
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
