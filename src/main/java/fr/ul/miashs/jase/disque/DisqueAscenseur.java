package fr.ul.miashs.jase.disque;

import java.util.List;

public class DisqueAscenseur implements OrdonnanceurDisque {

    private boolean sensDroite = true;

    @Override
    public RequeteDisque choisirRequete(List<RequeteDisque> requetes, int positionBras, String direction) {
        if (requetes.isEmpty()) return null;
        if (direction.equals("gauche")) {
            sensDroite = false;
        } else {
            sensDroite = true;
        }

        // On cherche les requêtes dans la direction courante
        RequeteDisque candidate = null;
        int bestDistance = Integer.MAX_VALUE;

        for (RequeteDisque rq : requetes) {
            int dist = rq.piste - positionBras;
            if (sensDroite && dist >= 0 && dist < bestDistance) {
                bestDistance = dist;
                candidate = rq;
            }
            if (!sensDroite && dist <= 0 && Math.abs(dist) < bestDistance) {
                bestDistance = Math.abs(dist);
                candidate = rq;
            }
        }
        if (candidate == null) {
            sensDroite = !sensDroite;
            return choisirRequete(requetes, positionBras, sensDroite ? "droite" : "gauche");
        }
        return candidate;
    }
}
