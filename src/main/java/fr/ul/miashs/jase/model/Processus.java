package fr.ul.miashs.jase.model;

import java.io.*;
import java.util.*;

public class Processus {
    public int id, arrivee, priorite;
    public int tempsDebutExecution = -1;
    public int tempsFin = -1;
    public List<Evenement> evenements = new ArrayList<>();

    public static List<Processus> chargerDepuisFichier(String fichier) throws IOException {
        List<Processus> resultat = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split("/");
                Processus p = new Processus();
                p.id = Integer.parseInt(parts[0]);
                p.arrivee = Integer.parseInt(parts[1]);
                p.priorite = Integer.parseInt(parts[2]);
                String evts = parts[3].replaceAll("[{}]", "");
                for (String evt : evts.split(",")) {
                    String nom = evt.split("\\(")[0];
                    int val = Integer.parseInt(evt.split("\\(")[1].replace(")", ""));
                    p.evenements.add(new Evenement(nom, val));
                }
                resultat.add(p);
            }
        }
        return resultat;
    }

    public void executer(int duree) {
        if (!evenements.isEmpty()) {
            Evenement evt = evenements.get(0);
            evt.param -= duree;
            if (evt.param <= 0) evenements.remove(0);
        }
    }

    public boolean estTermine() {
        return evenements.isEmpty();
    }
}