package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Entity capable of inflicting damage.
 */
public interface Attacker {

    /**
     * Strikes the given target.
     *
     * @param target The target receiving the attack.
     */
    void attack(Damageable target);
}
