package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implémentation FIFO pour le remplacement de page.
 */
public class RemplacementFIFO implements RemplacementStrategy {

    // On garde la liste des cadres dans l'ordre de chargement.
    private final Queue<Integer> fileFIFO = new LinkedList<>();

    @Override
    public void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant) {
        // Vérifier s'il y a un cadre libre
        int indexLibre = tablePages.trouverCadreLibre();
        if (indexLibre >= 0) {
            // On utilise un cadre libre
            chargerDansCadre(tablePages, indexLibre, p, numeroPage, tempsCourant);
            fileFIFO.offer(indexLibre);
        } else {
            // Sinon, on prend le plus ancien de la file
            Integer cadreARemplacer = fileFIFO.poll();
            // On le remplace
            chargerDansCadre(tablePages, cadreARemplacer, p, numeroPage, tempsCourant);
            // Et on le remet en queue
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
