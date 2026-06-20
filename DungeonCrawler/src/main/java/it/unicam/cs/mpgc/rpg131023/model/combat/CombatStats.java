package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Mutable state tracker for entity health and base damage.
 */
public class CombatStats {
    private int health;
    private final int damage;

    /**
     * Initializes the combat stats.
     *
     * @param health Initial hit points (1-100).
     * @param damage Base damage output.
     */
    public CombatStats(final int health, final int damage) {
        if (health <= 0 || health > 100) {
            throw new IllegalArgumentException("Health must be between 1 and 100 inclusive.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return this.damage;
    }

    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Damage amount must be greater than zero.");
        }
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombatStats that = (CombatStats) o;
        return health == that.health && damage == that.damage;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(health, damage);
    }
}
