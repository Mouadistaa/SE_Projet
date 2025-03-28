package fr.ul.miashs.jase.model;

/** Représente un événement (CALCUL, ECRITURE, etc.) avec un paramètre. */
public class Evenement {
    public enum Type {
        CALCUL, ECRITURE, LECTURE, DORMIR, FIN
    }
    public Type type;
    public int param;

    public Evenement(Type type, int param) {
        this.type = type;
        this.param = param;
    }
}
