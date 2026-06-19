package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Represents an entity that can take damage and have a health status.
 */
public interface Damageable {

    /**
     * Reduces the entity's health by the specified amount.
     *
     * @param amount The amount of damage to take.
     */
    public void takeDamage(int amount);

    /**
     * Checks if the entity is still alive.
     *
     * @return {@code true} if the entity has health greater than 0, {@code false} otherwise.
     */
    public boolean isAlive();
}
