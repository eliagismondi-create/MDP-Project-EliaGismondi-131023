package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.ResourceType;

/**
 * Rappresenta un Dungeon nel gioco, le cui istanze vengono popolate a runtime
 * tramite un approccio Data-Driven.
 */
public class Dungeon {
    private final String id;
    private final String name;
    private final Map<ResourceType, Integer> treasure;
    private final Map<EnemyType, Integer> enemySpawns;

    public Dungeon(final String id, final String name) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del dungeon non puo' essere nullo o vuoto.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del dungeon non puo' essere nullo o vuoto.");
        }
        this.id = id;
        this.name = name;
        this.treasure = new EnumMap<>(ResourceType.class);
        this.enemySpawns = new EnumMap<>(EnemyType.class);
    }

    /**
     * Aggiunge risorse al bottino (loot) del dungeon in modo sicuro (Fail-Fast).
     * 
     * @param type   Il tipo di risorsa.
     * @param amount La quantita' da aggiungere.
     */
    public void addLoot(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Il tipo di risorsa non puo' essere null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' deve essere maggiore di zero.");
        }
        this.treasure.put(type, this.treasure.getOrDefault(type, 0) + amount);
    }

    /**
     * Configura la quantita' di nemici previsti per questo dungeon.
     * 
     * @param enemyType Il tipo di nemico (es. GOBLIN, ORC).
     * @param quantity Il numero di nemici di quel tipo.
     */
    public void addEnemySpawn(final EnemyType enemyType, final int quantity) {
        if (enemyType == null) {
            throw new NullPointerException("Il tipo di nemico non puo' essere null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantita' deve essere maggiore di zero.");
        }
        this.enemySpawns.put(enemyType, this.enemySpawns.getOrDefault(enemyType, 0) + quantity);
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    /**
     * @return Mappa immutabile del tesoro.
     */
    public Map<ResourceType, Integer> getTreasure() {
        return Collections.unmodifiableMap(this.treasure);
    }

    /**
     * @return Mappa immutabile degli spawn dei nemici.
     */
    public Map<EnemyType, Integer> getEnemySpawns() {
        return Collections.unmodifiableMap(this.enemySpawns);
    }
}
