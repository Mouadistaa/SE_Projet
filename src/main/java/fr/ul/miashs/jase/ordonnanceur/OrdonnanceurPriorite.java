package fr.ul.miashs.jase.ordonnanceur;

import fr.ul.miashs.jase.model.Processus;
import java.util.Comparator;
import java.util.PriorityQueue;

public class OrdonnanceurPriorite implements Ordonnanceur {

    // Priorité la plus haute = exécution en premier
    private final PriorityQueue<Processus> file = new PriorityQueue<>(
            Comparator.comparingInt((Processus p) -> -p.priorite)
    );

    @Override
    public void ajouter(Processus p) {
        file.offer(p);
    }

    @Override
    public Processus suivant() {
        return file.poll();
    }
}
