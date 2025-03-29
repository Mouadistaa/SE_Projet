package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;

public interface StrategieRemplacement {
    /**
     * Choisit un cadre à remplacer dans tablePages, et y installe la page "numeroPage" du processus "p".
     */
    void remplacerPage(TablePages tablePages, Processus p, int numeroPage, int tempsCourant);
}
