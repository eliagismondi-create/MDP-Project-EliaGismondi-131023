package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.utils.StatsLoader;

/**
 * Data-driven factory for enemy instantiation.
 */
public final class EnemyFactory {

    private EnemyFactory() {
        // Impedisce l'istanziazione
    }

    /**
     * Instantiates an enemy based on its textual identifier.
     *
     * @param enemyId Textual identifier (e.g. "goblin").
     * @return Fully configured enemy instance.
     */
    public static Enemy create(final String enemyId) {
        if (enemyId == null || enemyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Enemy ID cannot be null or empty.");
        }

        final CombatStats stats = StatsLoader.getStatsFor(enemyId);
        final EnemyType type = EnemyType.valueOf(enemyId.toUpperCase());

        return new Enemy(stats, type);
    }
}
