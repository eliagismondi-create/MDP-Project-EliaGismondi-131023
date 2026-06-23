package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceCollector;

/**
 * Game level containing loot and enemy spawn configurations.
 */
public class Dungeon {
    public enum Difficulty {
        NORMAL, HARD
    }

    private final String id;
    private final String name;
    private final String description;
    private final Difficulty difficulty;
    private final List<Loot> treasures;
    private final Map<String, Integer> enemySpawns;

    public Dungeon(final String id, final String name, final String description, final Difficulty difficulty) {
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
        if (difficulty == null) {
            throw new NullPointerException("Difficulty cannot be null.");
        }
        this.difficulty = difficulty;
        this.treasures = new ArrayList<>();
        this.enemySpawns = new java.util.HashMap<>();
    }

    /**
     * Appends loot to the level pool.
     * 
     * @param loot Item to add.
     */
    public void addLoot(final Loot loot) {
        if (loot == null) {
            throw new NullPointerException("Loot cannot be null.");
        }
        this.treasures.add(loot);
    }

    /**
     * Configures spawn amounts for a specific enemy variant.
     * 
     * @param enemyType Enemy variant.
     * @param quantity  Spawn count.
     */
    public void addEnemySpawn(final String enemyType, final int quantity) {
        if (enemyType == null) {
            throw new NullPointerException("Enemy type cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.enemySpawns.put(enemyType, this.enemySpawns.getOrDefault(enemyType, 0) + quantity);
    }

    /**
     * Distributes all collected treasures to the given collector.
     * 
     * @param collector Target character receiving the loot.
     */
    public void claimLoot(final ResourceCollector collector) {
        if (collector == null) {
            throw new NullPointerException("Collector cannot be null.");
        }
        this.treasures.forEach(loot -> loot.applyTo(collector));
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

    public Difficulty getDifficulty() {
        return this.difficulty;
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
    public Map<String, Integer> getEnemySpawns() {
        return Collections.unmodifiableMap(this.enemySpawns);
    }

    /**
     * @return The next enemy type to spawn.
     */
    public String getNextEnemyType() {
        if (this.enemySpawns.isEmpty()) {
            return null;
        }
        return this.enemySpawns.keySet().iterator().next();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Dungeon dungeon = (Dungeon) o;
        return id.equals(dungeon.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
