package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;
import java.util.LinkedList;
import java.util.Queue;

public class OrdonnanceurTourniquet implements Ordonnanceur {

    private final Queue<Processus> file = new LinkedList<>();
    private final int quantum;

    public OrdonnanceurTourniquet(int quantum) {
        this.quantum = quantum;
    }

    @Override
    public void ajouter(Processus p) {
        file.offer(p);
    }

    @Override
    public Processus suivant() {
        return file.poll();
    }
}
