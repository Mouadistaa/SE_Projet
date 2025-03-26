package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;
import java.util.*;

public class OrdonnanceurFIFO implements Ordonnanceur {
    private final Queue<Processus> file = new LinkedList<>();

    public void ajouter(Processus p) {
        file.offer(p);
    }

    public Processus suivant() {
        return file.poll();
    }
}