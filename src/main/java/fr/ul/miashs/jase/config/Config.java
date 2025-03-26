package fr.ul.miashs.jase.config;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Config {
    private final Map<String, String> params = new HashMap<>();

    public Config(String path) throws IOException {
        for (String line : Files.readAllLines(Path.of(path))) {
            if (line.contains("=")) {
                String[] parts = line.split("=");
                params.put(parts[0].trim(), parts[1].trim());
            }
        }
    }

    public int getTempsSimulation() {
        return Integer.parseInt(params.get("temps-simulation"));
    }
    public int getQuantum() {
        return Integer.parseInt(params.getOrDefault("processus-quantum", "100"));
    }
    public int getInterruptionHorloge() {
        return Integer.parseInt(params.getOrDefault("interruption-horloge", "100"));
    }
    public String getOrdonnancement() {
        return params.get("processus-ordonnancement");
    }
}
