package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Immutable object containing basic combat statistics.
 * The optional {@code className} allows the factory to resolve the concrete enemy class.
 */
public final class CombatStats {
    private final int health;
    private final int damage;
    private final String className;

    /**
     * Constructs the combat statistics and validates the parameters.
     *
     * @param health    The initial health (1-100).
     * @param damage    The base damage (>= 0).
     * @param className The fully-qualified class name of the enemy (can be null for the Hero).
     */
    public CombatStats(final int health, final int damage, final String className) {
        if (health <= 0 || health > 100) {
            throw new IllegalArgumentException("Health must be between 1 and 100 inclusive.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        this.health = health;
        this.damage = damage;
        this.className = className;
    }

    /**
     * Constructs the combat statistics without a class name.
     *
     * @param health The initial health (1-100).
     * @param damage The base damage (>= 0).
     */
    public CombatStats(final int health, final int damage) {
        this(health, damage, null);
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    /**
     * @return The fully-qualified class name of the entity, or {@code null} if not specified.
     */
    public String getClassName() {
        return this.className;
    }
}
