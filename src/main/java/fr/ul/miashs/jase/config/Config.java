package fr.ul.miashs.jase.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Config {
    private final Map<String, String> params = new HashMap<>();

    public Config(String path) throws IOException {
        for (String line : Files.readAllLines(Path.of(path))) {
            line = line.strip();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            if (!line.contains("=")) {
                continue;
            }
            String[] parts = line.split("=", 2);
            String cle = parts[0].trim();
            String val = parts[1].trim();
            params.put(cle, val);
        }
    }

    public int getTempsSimulation() {
        return Integer.parseInt(params.getOrDefault("temps-simulation", "1000"));
    }

    public int getQuantum() {
        return Integer.parseInt(params.getOrDefault("processus-quantum", "100"));
    }

    public int getInterruptionHorloge() {
        return Integer.parseInt(params.getOrDefault("interruption-horloge", "50"));
    }

    public String getOrdonnancement() {
        return params.getOrDefault("processus-ordonnancement", "FIFO");
    }

    // Ajoute cette méthode :
    public int getTempsEcritureDisque() {
        return Integer.parseInt(params.getOrDefault("temps-écriture-disque", "8"));
    }

    public int getTempsChargePage() {
        return Integer.parseInt(params.getOrDefault("temps-charge-page", "5"));
    }

    public int getPaginationNombreCadres() {
        return Integer.parseInt(params.getOrDefault("pagination-nombre-cadres", "8"));
    }

    public String getPaginationPolitiqueAllocation() {
        return params.getOrDefault("pagination-politique-allocation", "globale");
    }

    public String getPaginationAlgorithme() {
        return params.getOrDefault("pagination-algorithme", "FIFO");
    }

    public int getPaginationNombreCadresLocale() {
        return Integer.parseInt(params.getOrDefault("pagination-nombre-cadres-locale", "3"));
    }

    public int getDisquePositionInitiale() {
        return Integer.parseInt(params.getOrDefault("disque-position-initiale", "0"));
    }

    public String getDisqueDirectionInitiale() {
        return params.getOrDefault("disque-direction-initiale", "droite");
    }

    public String getDisqueOrdonnancement() {
        return params.getOrDefault("disque-ordonnancement", "FIFO");
    }

}
