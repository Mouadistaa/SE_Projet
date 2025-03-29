package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.config.Config;
import fr.ul.miashs.jase.model.Processus;

/**
 * Classe principale gérant la mémoire paginée.
 */
public class GestionMemoire {
    private final int nombreCadres;
    private final int tempsChargePage;
    private final StrategieRemplacement strategie;
    private final String politiqueAllocation; // "locale" ou "globale"

    private final TablePages tablePagesGlobal; // exemple d'approche globale

    public GestionMemoire(Config config) {
        this.nombreCadres = config.getPaginationNombreCadres();
        this.tempsChargePage = config.getTempsChargePage();
        this.politiqueAllocation = config.getPaginationPolitiqueAllocation();

        String algo = config.getPaginationAlgorithme();
        switch (algo) {
            case "FIFO":
                this.strategie = new StrategieRemplacementFIFO();
                break;
            case "seconde-chance":
                this.strategie = new StrategieRemplacementSecondeChance();
                break;
            case "non-récemment-utilisée":
                this.strategie = new StrategieRemplacementNRU();
                break;
            case "optimal":
                this.strategie = new StrategieRemplacementOptimal();
                break;
            default:
                this.strategie = new StrategieRemplacementFIFO();
                System.err.println("Algorithme mémoire inconnu, utilisation de FIFO par défaut.");
        }

        // Initialiser ta table de pages globale
        this.tablePagesGlobal = new TablePages(nombreCadres);
    }

    /**
     * Gérer la requête LECTURE(page) pour un processus.
     * Retourne le temps de chargement si défaut de page, sinon 0.
     */
    public int gererLecturePage(Processus p, int numeroPage, int tempsCourant) {
        // Vérifier si la page est déjà chargée
        boolean enMemoire = tablePagesGlobal.estEnMemoire(p, numeroPage);
        if (enMemoire) {
            return 0;
        } else {
            // Il faut charger la page => défaut de page
            // => on appelle la stratégie de remplacement
            // => on “bloque” le processus pendant tempsChargePage
            // => on met à jour la table de pages
            strategie.remplacerPage(tablePagesGlobal, p, numeroPage, tempsCourant);
            return tempsChargePage;
        }
    }

}
