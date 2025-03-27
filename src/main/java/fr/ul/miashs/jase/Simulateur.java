package fr.ul.miashs.jase;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.model.Processus;
import fr.ul.miashs.jase.ordonnanceur.Ordonnanceur;
import java.util.List;
import java.util.Iterator;

public class Simulateur {
    private final Config config;
    private final Ordonnanceur ordonnanceur;
    private final List<Processus> tousLesProcessus;
    // éventuellement : filePrêt, terminés, etc.
    private StringBuilder trace;
    private List<Processus> termines;
    // d’autres champs selon besoin

    public Simulateur(Config config, Ordonnanceur ordonnanceur, List<Processus> listeProcess) {
        this.config = config;
        this.ordonnanceur = ordonnanceur;
        this.tousLesProcessus = listeProcess;
        this.trace = new StringBuilder();
        // initialisation des listes...
    }

    public void run() {
        int tempsActuel = 0;
        int interruption = config.getInterruptionHorloge();
        int quantum = config.getQuantum();
        // Processus actif = null; etc.

        // Boucle de simulation
        while (tempsActuel < config.getTempsSimulation()) {
            // 1) Ajout des nouveaux processus arrivés
            // 2) Sélection ou préemption du processus actif
            // 3) Exécution / ou IDLE
            // 4) Avancée du temps
        }

        // Traitement final, calcul stats, etc.
    }

    public void afficherResultats() {
        // Affiche la trace, les stats, etc.
    }
}
