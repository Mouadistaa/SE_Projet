package fr.ul.miashs.jase;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.model.Processus;
import fr.ul.miashs.jase.ordonnanceur.*;
import java.io.IOException;
import java.util.*;

public class JASE {
    public static void main(String[] args) {
        System.out.println("DÉMARRAGE DU MAIN...");
        // Vérification des arguments
        if (args.length != 2) {
            System.out.println("Il faut utiliser: java -cp target/classes fr.ul.miashs.jase.Main config.txt programmes.txt");
            return;
        }
        String configPath = args[0];
        String dataPath   = args[1];
        try {
            // Charger la configuration
            Config config = new Config(configPath);

            // Charger la liste de processus
            List<Processus> tousLesProcessus = Processus.chargerDepuisFichier(dataPath);

            // Choisir un ordonnanceur en fonction du paramètre
            Ordonnanceur ordonnanceur;
            String algo = config.getOrdonnancement();
            switch (algo) {
                case "FIFO":
                    ordonnanceur = new OrdonnanceurFIFO();
                    break;
                case "tourniquet":
                    ordonnanceur = new OrdonnanceurTourniquet(config.getQuantum());
                    break;
                case "par-priorités":
                    ordonnanceur = new OrdonnanceurPriorite();
                    break;
                default:
                    throw new IllegalArgumentException("Pas le bon ordonnanceur");
            }

            // Créer et lancer le simulateur
            Simulateur simu = new Simulateur(config, ordonnanceur, tousLesProcessus);
            simu.run();
            simu.afficherResultats();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Erreur de format dans le fichier : " + e.getMessage());
        }
    }
}
