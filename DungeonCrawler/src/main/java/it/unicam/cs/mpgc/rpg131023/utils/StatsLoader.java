package it.unicam.cs.mpgc.rpg131023.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Utility per il caricamento delle statistiche da file JSON.
 * Rispettando l'SRP, questa classe e' l'unica responsabile del parsing dei dati
 * di configurazione.
 */
public final class StatsLoader {
    private static final String STATS_FILE = "/stats.json";
    private static Map<String, CombatStats> statsCache = null;

    private StatsLoader() {
        // Nasconde il costruttore pubblico per questa classe di utilità
    }

    /**
     * Carica e restituisce le statistiche per un'entita' specifica.
     * I dati vengono caricati in modo lazy e cachati per chiamate successive.
     * 
     * @param entityId L'identificatore dell'entita' (es. "hero", "goblin").
     * @return Le statistiche associate all'entita'.
     * @throws IllegalStateException se il file non puo' essere letto o l'entita'
     *                               non esiste.
     */
    public static CombatStats getStatsFor(final String entityId) {
        if (statsCache == null) {
            loadStats();
        }
        final CombatStats stats = statsCache.get(entityId.toLowerCase());
        if (stats == null) {
            throw new IllegalStateException("Nessuna statistica trovata per l'entita': " + entityId);
        }
        return stats;
    }

    private static void loadStats() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(StatsLoader.class.getResourceAsStream(STATS_FILE),
                        "File " + STATS_FILE + " not found in classpath."))) {

            final Gson gson = new Gson();
            final Type type = new TypeToken<Map<String, CombatStats>>() {
            }.getType();
            statsCache = gson.fromJson(reader, type);

            if (statsCache == null || statsCache.isEmpty()) {
                throw new IllegalStateException("Stats file is empty or malformed.");
            }
        } catch (java.io.IOException | com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Critical error loading stats from " + STATS_FILE,
                    e);
        }
    }
}
