package fr.ul.miashs.jase.disque;

import java.util.List;

public interface OrdonnanceurDisque {
    /**
     * Choisit la requête à traiter en fonction de la liste
     * et de la position courante du bras.
     */
    RequeteDisque choisirRequete(List<RequeteDisque> requetes,
                                 int positionBras,
                                 String direction);
}
