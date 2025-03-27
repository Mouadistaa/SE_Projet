package fr.ul.miashs.jase.disque;

import java.util.List;

public class DisquePlusProche implements OrdonnanceurDisque {

    @Override
    public RequeteDisque choisirRequete(List<RequeteDisque> requetes, int positionBras, String direction) {
        if (requetes.isEmpty()) return null;
        RequeteDisque best = null;
        int bestDist = Integer.MAX_VALUE;
        for (RequeteDisque rq : requetes) {
            int dist = Math.abs(rq.piste - positionBras);
            if (dist < bestDist) {
                bestDist = dist;
                best = rq;
            }
        }
        return best;
    }
}
