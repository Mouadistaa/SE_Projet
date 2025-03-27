package fr.ul.miashs.jase.disque;

import fr.ul.miashs.jase.model.Processus;

public class RequeteDisque {
    public final Processus demandeur;
    public final int piste;

    public RequeteDisque(Processus p, int piste) {
        this.demandeur = p;
        this.piste = piste;
    }
}
