package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

/**
 * Rappresenta l'entità Orco nel gioco.
 * Estende AbstractEnemy ereditando la logica di combattimento e incapsula
 * l'identità specifica del nemico.
 */
public class Orc extends AbstractEnemy {

    /**
     * Costruisce un Orco tramite Dependency Injection.
     *
     * @param stats Le statistiche base dell'Orco (salute e danno).
     */
    public Orc(final CombatStats stats) {
        super(stats);
    }
}
