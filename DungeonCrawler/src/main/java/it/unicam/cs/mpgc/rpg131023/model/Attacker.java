package it.unicam.cs.mpgc.rpg131023.model;

/**
 * Rappresenta un'entita' capace di infliggere danni a un bersaglio {@link Damageable}.
 * Introdotta per risolvere la violazione dell'Interface Segregation Principle (ISP).
 */
public interface Attacker {

    /**
     * Infligge danni al bersaglio specificato.
     *
     * @param target Il bersaglio da attaccare.
     */
    void attack(Damageable target);
}
