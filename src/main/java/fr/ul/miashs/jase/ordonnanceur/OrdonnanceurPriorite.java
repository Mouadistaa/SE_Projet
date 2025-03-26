package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;

import java.util.*;

public class OrdonnanceurPriorite implements Ordonnanceur {
    private final PriorityQueue<Processus> file = new PriorityQueue<>(Comparator.comparingInt(p -> -p.priorite));

    public void ajouter(Processus p) {
        file.offer(p);
    }

    public Processus suivant() {
        return file.poll();
    }
}

