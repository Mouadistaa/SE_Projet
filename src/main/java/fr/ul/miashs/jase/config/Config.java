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
                continue; // ignorer lignes vides/commentaires
            }
            if (!line.contains("=")) {
                System.err.println("Ligne invalide (pas de '=') : " + line);
                continue;
            }
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                String cle = parts[0].trim();
                String val = parts[1].trim();
                params.put(cle, val);
            }
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
}
