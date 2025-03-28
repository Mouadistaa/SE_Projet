package fr.ul.miashs.jase.memoire;

import fr.ul.miashs.jase.model.Processus;
import java.util.*;


public class TablePages {
    private final Cadre[] cadres;

    public TablePages(int nombreCadres) {
        this.cadres = new Cadre[nombreCadres];
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

    public boolean estEnMemoire(Processus p, int numeroPage) {
        for (Cadre c : cadres) {
            if (c.occupe && c.proprietaire == p && c.numeroPage == numeroPage) {
                return true;
            }
        }
        return false;
    }

    public int trouverCadreLibre() {
        for (int i = 0; i < cadres.length; i++) {
            if (!cadres[i].occupe) {
                return i;
            }
        }
        return -1; // pas de cadre libre
    }

    public Cadre getCadre(int index) {
        return cadres[index];
    }

    public int getNombreCadres() {
        return cadres.length;
    }
}
