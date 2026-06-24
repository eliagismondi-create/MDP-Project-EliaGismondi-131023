package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

import java.util.Objects;

/**
 * Concrete enemy entity extending the shared combat logic base class.
 */
public class Enemy extends AbstractCombatant {

    private final String type;

    /**
     * Creates an enemy with the given stats and type identifier.
     *
     * @param stats The base combat stats.
     * @param type  The enemy variant name (e.g. "GOBLIN").
     */
    public Enemy(final CombatStats stats, final String type) {
        super(stats);
        if (type == null) {
            throw new NullPointerException("Enemy type cannot be null.");
        }
        this.type = type;
    }

    /**
     * @return The enemy variant type.
     */
    public String getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enemy enemy = (Enemy) o;
        return type.equals(enemy.type) && getHealth() == enemy.getHealth()
                && getDamage() == enemy.getDamage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, getHealth(), getDamage());
    }
}
