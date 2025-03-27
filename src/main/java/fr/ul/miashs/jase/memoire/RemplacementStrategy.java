package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;

public interface RemplacementStrategy {
    /**
     * Choisit un cadre à remplacer dans tablePages, et y installe la page "numeroPage" du processus "p".
     * "tempsCourant" peut aider à mettre à jour le champ "derniereUtilisation".
     */
    void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant);
}
