package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.utils.StatsService;

/**
 * Data-driven factory for enemy instantiation.
 */
public class EnemyFactory {
    
    private final StatsService statsService;

    public EnemyFactory(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * Instantiates an enemy based on its textual identifier.
     *
     * @param enemyId Textual identifier (e.g. "goblin").
     * @return Fully configured enemy instance.
     */
    public Enemy create(final String enemyId) {
        if (enemyId == null || enemyId.trim().isEmpty()) {
            throw new IllegalArgumentException("Enemy ID cannot be null or empty.");
        }

        final CombatStats stats = this.statsService.getStatsFor(enemyId);
        return new Enemy(stats, enemyId.toUpperCase());
    }
}
