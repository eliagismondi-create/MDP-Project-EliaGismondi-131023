package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Represents an entity capable of dealing damage to a {@link Damageable} target.
 */
public interface Attacker {

    /**
     * Deals damage to the specified target.
     *
     * @param target The target to attack.
     */
    void attack(Damageable target);
}
