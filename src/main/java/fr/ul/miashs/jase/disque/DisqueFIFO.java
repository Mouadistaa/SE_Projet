package fr.ul.miashs.jase.disque;

import java.util.List;

public class DisqueFIFO implements OrdonnanceurDisque {

    @Override
    public RequeteDisque choisirRequete(List<RequeteDisque> requetes, int positionBras, String direction) {
        if (requetes.isEmpty()) return null;
        // On prend la première arrivée
        return requetes.get(0);
    }
}
