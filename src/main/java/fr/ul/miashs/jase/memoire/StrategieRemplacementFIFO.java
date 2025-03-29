package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implémentation FIFO pour le remplacement de page.
 */
public class StrategieRemplacementFIFO implements StrategieRemplacement {

    private final Queue<Integer> fileFIFO = new LinkedList<>();

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {
        int indexLibre = tablePages.trouverCadreLibre();
        if (indexLibre >= 0) {
            chargerDansCadre(tablePages, indexLibre, p, numeroPage, tempsCourant);
            fileFIFO.offer(indexLibre);
        } else {
            Integer cadreARemplacer = fileFIFO.poll();
            chargerDansCadre(tablePages, cadreARemplacer, p, numeroPage, tempsCourant);
            fileFIFO.offer(cadreARemplacer);
        }
    }

    private void chargerDansCadre(TablePages tablePages, int indexCadre, Processus p, int numeroPage, int tempsCourant) {
        TablePages.Cadre c = tablePages.getCadre(indexCadre);
        c.occupe = true;
        c.proprietaire = p;
        c.numeroPage = numeroPage;
        c.derniereUtilisation = tempsCourant;
        c.bitReference = true;
        c.bitModifie = false;
    }
}
