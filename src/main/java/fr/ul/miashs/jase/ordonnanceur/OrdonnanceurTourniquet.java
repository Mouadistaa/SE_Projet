package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;

import java.util.*;

public class OrdonnanceurTourniquet implements Ordonnanceur {
    private final Queue<Processus> file = new LinkedList<>();
    private final int quantum;

    public OrdonnanceurTourniquet(int quantum) {
        this.quantum = quantum;
    }

    public void ajouter(Processus p) {
        file.offer(p);
    }

    public Processus suivant() {
        return file.poll();
    }
}
