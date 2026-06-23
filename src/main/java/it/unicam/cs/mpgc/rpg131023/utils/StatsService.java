package it.unicam.cs.mpgc.rpg131023.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Service for loading and providing configuration stats from JSON.
 * Replaces the old static StatsLoader to avoid global state.
 */
public class StatsService {
    private final Map<String, CombatStats> statsCache;

    /**
     * Initializes the stats cache from a provided Reader.
     * 
     * @param reader The Reader containing JSON stats data.
     */
    public StatsService(Reader reader) {
        try {
            final Gson gson = new Gson();
            final Type type = new TypeToken<Map<String, CombatStats>>() {
            }.getType();
            this.statsCache = gson.fromJson(reader, type);

            if (this.statsCache == null || this.statsCache.isEmpty()) {
                throw new IllegalStateException("Stats file is empty or malformed.");
            }
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Critical error parsing stats JSON.", e);
        }
    }

    /**
     * Returns the stats for a specific entity.
     * 
     * @param entityId The identifier of the entity (e.g. "hero", "goblin").
     * @return The stats associated with the entity.
     */
    public CombatStats getStatsFor(final String entityId) {
        final CombatStats stats = this.statsCache.get(entityId.toLowerCase());
        if (stats == null) {
            throw new IllegalStateException("No stats found for entity: " + entityId);
        }
        return stats;
    }
}
