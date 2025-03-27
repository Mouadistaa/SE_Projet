package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;

/**
 * Interface commune à tous les algorithmes d’ordonnancement CPU.
 */
public interface Ordonnanceur {
    void ajouter(Processus p);
    Processus suivant();
}
