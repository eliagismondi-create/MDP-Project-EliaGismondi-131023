package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;

/**
 * Represents a dungeon in the game.
 */
public class Dungeon {
    private final String id;
    private final String name;
    private final String description;
    private final List<Loot> treasures;
    private final Map<EnemyType, Integer> enemySpawns;

    public Dungeon(final String id, final String name, final String description) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Dungeon ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Dungeon name cannot be null or empty.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Dungeon description cannot be null or empty.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.treasures = new ArrayList<>();
        this.enemySpawns = new EnumMap<>(EnemyType.class);
    }

    /**
     * Adds loot to the dungeon.
     * 
     * @param loot The loot to add.
     */
    public void addLoot(final Loot loot) {
        if (loot == null) {
            throw new NullPointerException("Loot cannot be null.");
        }
        this.treasures.add(loot);
    }

    /**
     * Configures the number of enemies to spawn for this dungeon.
     * 
     * @param enemyType The type of enemy.
     * @param quantity  The number of enemies of that type.
     */
    public void addEnemySpawn(final EnemyType enemyType, final int quantity) {
        if (enemyType == null) {
            throw new NullPointerException("Enemy type cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
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
     * @return An unmodifiable list of treasures.
     */
    public List<Loot> getTreasures() {
        return Collections.unmodifiableList(this.treasures);
    }

    /**
     * @return An unmodifiable map of enemy spawns.
     */
    public Map<EnemyType, Integer> getEnemySpawns() {
        return Collections.unmodifiableMap(this.enemySpawns);
    }
}
