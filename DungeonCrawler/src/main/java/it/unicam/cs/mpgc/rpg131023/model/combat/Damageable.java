package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Target capable of receiving damage and having a health state.
 */
public interface Damageable {

    /**
     * Applies damage to the current health pool.
     *
     * @param amount Damage to take.
     */
    public void takeDamage(int amount);

    /**
     * Checks if health is above zero.
     *
     * @return True if alive.
     */
    public boolean isAlive();
}
