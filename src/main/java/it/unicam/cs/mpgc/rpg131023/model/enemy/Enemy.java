package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.Attacker;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.combat.Damageable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import it.unicam.cs.mpgc.rpg131023.model.combat.Combatant;

/**
 * Concrete enemy entity delegating combat math to composition stats.
 */
public class Enemy implements Combatant {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final CombatStats stats;
    private final String type;

    public Enemy(final CombatStats stats, final String type) {
        if (stats == null) {
            throw new NullPointerException("Combat stats cannot be null.");
        }
        if (type == null) {
            throw new NullPointerException("Enemy type cannot be null.");
        }
        this.stats = new CombatStats(stats.getHealth(), stats.getDamage());
        this.type = type;
    }

    public String getType() {
        return this.type;
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

    public int getDamage() {
        return this.stats.getDamage();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enemy enemy = (Enemy) o;
        return type == enemy.type && stats.equals(enemy.stats);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(stats, type);
    }
}
