package it.unicam.cs.mpgc.rpg131023.model.combat;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Base class for combat entities.
 * Provides default implementations for {@link Damageable} and {@link Attacker}.
 */
public abstract class AbstractCombatant implements Damageable, Attacker {
    private final IntegerProperty health = new SimpleIntegerProperty();
    private final int damage;

    /**
     * Constructs a new combatant.
     *
     * @param stats The combat statistics. Must not be null.
     */
    public AbstractCombatant(final CombatStats stats) {
        if (stats == null) {
            throw new NullPointerException("Combat stats cannot be null.");
        }
        this.health.set(stats.getHealth());
        this.damage = stats.getDamage();
    }

    @Override
    public void attack(Damageable target) {
        if (!isAlive()) {
            throw new IllegalStateException("Attacker is dead and cannot attack.");
        }
        if (target == null) {
            throw new NullPointerException("Target cannot be null.");
        }
        if (!target.isAlive()) {
            throw new IllegalArgumentException("Target is already dead.");
        }

        target.takeDamage(getDamage());
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Damage amount must be greater than zero.");
        }
        if (!isAlive()) {
            throw new IllegalStateException("Entity is already dead.");
        }

        this.health.set(this.health.get() - amount);
        if (this.health.get() < 0) {
            this.health.set(0);
        }
    }

    @Override
    public boolean isAlive() {
        return this.health.get() > 0;
    }

    public int getHealth() {
        return this.health.get();
    }

    public IntegerProperty healthProperty() {
        return this.health;
    }

    /**
     * Sets the health of the combatant.
     *
     * @param health The new health value.
     */
    protected void setHealth(final int health) {
        this.health.set(health);
    }

    public int getDamage() {
        return this.damage;
    }
}
