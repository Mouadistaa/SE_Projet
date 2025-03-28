package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;
import java.util.LinkedList;
import java.util.Queue;

public class OrdonnanceurFIFO implements Ordonnanceur {

    private final Queue<Processus> file = new LinkedList<>();

    @Override
    public void ajouter(Processus p) {

        file.offer(p);
    }

    @Override
    public Processus suivant() {

        return file.poll();
    }
}
