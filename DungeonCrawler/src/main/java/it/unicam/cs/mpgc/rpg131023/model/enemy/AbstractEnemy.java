package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.Damageable;

public abstract class AbstractEnemy implements Damageable {
    private final int damage;
    private int health;

    public AbstractEnemy(final CombatStats stats) {
        if (stats == null) {
            throw new NullPointerException("Le statistiche di combattimento non possono essere null.");
        }
        this.damage = stats.getDamage();
        this.health = stats.getHealth();
    }

    public void attack(Damageable target) {
        if (!isAlive())
            throw new IllegalStateException("Enemy is dead and cannot attack");
        if (!target.isAlive())
            throw new IllegalArgumentException("Target is already dead");

        target.takeDamage(this.damage);
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Damage amount must be higher than zero");
        if (!this.isAlive())
            throw new IllegalStateException("Enemy is already dead");

        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Metodi getter

    public boolean isAlive() {
        return this.health > 0;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHealth() {
        return this.health;
    }

}
