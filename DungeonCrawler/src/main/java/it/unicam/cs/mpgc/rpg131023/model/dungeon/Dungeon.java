package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;

/**
 * Represents a dungeon in the game.
 */
public class Dungeon {
    public enum Difficulty {
        NORMAL, HARD
    }

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

    /**
     * Distribuisce i tesori del dungeon all'eroe passato.
     * 
     * @param hero L'eroe a cui distribuire il bottino.
     */
    public void claimLoot(final Hero hero) {
        if (hero == null) {
            throw new NullPointerException("Hero cannot be null.");
        }
        this.treasures.forEach(loot -> loot.applyTo(hero));
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
        return this.id.toLowerCase().contains("bandit") ? Difficulty.NORMAL : Difficulty.HARD;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dungeon dungeon = (Dungeon) o;
        return id.equals(dungeon.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}
