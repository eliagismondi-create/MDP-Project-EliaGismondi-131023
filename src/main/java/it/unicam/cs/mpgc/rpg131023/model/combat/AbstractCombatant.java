package it.unicam.cs.mpgc.rpg131023.model.combat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base wrapper bridging combat logic and property change events.
 */
public abstract class AbstractCombatant implements Damageable, Attacker {
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final CombatStats stats;

    /**
     * Creates a combatant with deep-copied stats.
     *
     * @param stats The base stats to copy.
     */
    public AbstractCombatant(final CombatStats stats) {
        if (stats == null) {
            throw new NullPointerException("Combat stats cannot be null.");
        }
        // Deep copy the stats so this entity has its own mutable state
        this.stats = new CombatStats(stats.getHealth(), stats.getDamage());
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

        int oldHealth = this.stats.getHealth();
        this.stats.takeDamage(amount);
        support.firePropertyChange("health", oldHealth, this.stats.getHealth());
    }

    @Override
    public boolean isAlive() {
        return this.stats.isAlive();
    }

    public int getHealth() {
        return this.stats.getHealth();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    /**
     * Overrides current health and triggers property listeners.
     *
     * @param health The new health value.
     */
    public void setHealth(final int health) {
        int oldHealth = this.stats.getHealth();
        this.stats.setHealth(health);
        support.firePropertyChange("health", oldHealth, this.stats.getHealth());
    }

    public int getDamage() {
        return this.stats.getDamage();
    }
}
