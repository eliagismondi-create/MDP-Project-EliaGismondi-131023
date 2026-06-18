package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;

/**
 * Rappresenta un Dungeon nel gioco, le cui istanze vengono popolate a runtime
 * tramite un approccio Data-Driven.
 */
public class Dungeon {
    private final String id;
    private final String name;
    private final String description;
    private final List<Loot> treasures;
    private final Map<EnemyType, Integer> enemySpawns;

    public Dungeon(final String id, final String name, final String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del dungeon non puo' essere nullo o vuoto.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del dungeon non puo' essere nullo o vuoto.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descrizione del dungeon non puo' essere nulla o vuota.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.treasures = new ArrayList<>();
        this.enemySpawns = new EnumMap<>(EnemyType.class);
    }

    /**
     * Aggiunge un bottino al dungeon in modo sicuro (Fail-Fast).
     * 
     * @param loot L'oggetto Loot da aggiungere.
     */
    public void addLoot(final Loot loot) {
        if (loot == null) {
            throw new NullPointerException("Il loot non puo' essere null.");
        }
        this.treasures.add(loot);
    }

    /**
     * Configura la quantita' di nemici previsti per questo dungeon.
     * 
     * @param enemyType Il tipo di nemico (es. GOBLIN, ORC).
     * @param quantity  Il numero di nemici di quel tipo.
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

    public String getDescription() {
        return this.description;
    }

    /**
     * @return Lista immutabile dei tesori.
     */
    public List<Loot> getTreasures() {
        return Collections.unmodifiableList(this.treasures);
    }

    /**
     * @return Mappa immutabile degli spawn dei nemici.
     */
    public Map<EnemyType, Integer> getEnemySpawns() {
        return Collections.unmodifiableMap(this.enemySpawns);
    }
}
