package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;
import java.util.*;

/**
 * Représente la table de pages globale ou locale,
 * selon la façon dont tu veux structurer les infos.
 */
public class TablePages {
    // Par exemple, on stocke un tableau de "Cadre" (frame)
    private final Cadre[] cadres;

    public TablePages(int nombreCadres) {
        this.cadres = new Cadre[nombreCadres];
        // Initialiser chaque cadre
        for (int i = 0; i < nombreCadres; i++) {
            cadres[i] = new Cadre();
        }
    }

    public static class Cadre {
        public boolean occupe = false;
        public Processus proprietaire = null;
        public int numeroPage = -1;
        public int derniereUtilisation = 0;
        public boolean bitReference = false;
        public boolean bitModifie = false;
    }

    /**
     * Vérifie si la page "numeroPage" du processus "p" est déjà en mémoire.
     */
    public boolean estEnMemoire(Processus p, int numeroPage) {
        for (Cadre c : cadres) {
            if (c.occupe && c.proprietaire == p && c.numeroPage == numeroPage) {
                return true;
            }
        }
        return false;
    }

    // Méthodes pour trouver un cadre libre, etc.
    public int trouverCadreLibre() {
        for (int i = 0; i < cadres.length; i++) {
            if (!cadres[i].occupe) {
                return i;
            }
        }
        return -1; // pas de cadre libre
    }

    // D'autres méthodes selon tes besoins...
    public Cadre getCadre(int index) {
        return cadres[index];
    }

    public int getNombreCadres() {
        return cadres.length;
    }
}
