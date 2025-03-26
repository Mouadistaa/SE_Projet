package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;

public interface Ordonnanceur {
    void ajouter(Processus p);
    Processus suivant();
}