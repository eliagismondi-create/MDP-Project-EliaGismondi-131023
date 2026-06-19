package it.unicam.cs.mpgc.rpg131023.model.combat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for combat entities.
 * Provides default implementations for {@link Damageable} and {@link Attacker}.
 */
public abstract class AbstractCombatant implements Damageable, Attacker {
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private int health;
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
        this.health = stats.getHealth();
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

        int oldHealth = this.health;
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
        support.firePropertyChange("health", oldHealth, this.health);
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    public int getHealth() {
        return this.health;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Sets the health of the combatant.
     *
     * @param health The new health value.
     */
    public void setHealth(final int health) {
        int oldHealth = this.health;
        this.health = health;
        support.firePropertyChange("health", oldHealth, this.health);
    }

    public int getDamage() {
        return this.damage;
    }
}
