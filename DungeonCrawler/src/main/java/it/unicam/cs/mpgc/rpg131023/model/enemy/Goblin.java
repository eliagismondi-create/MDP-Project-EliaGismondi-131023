package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.CombatStats;

/**
 * Rappresenta l'entità Goblin nel gioco.
 * Estende AbstractEnemy ereditando la logica di combattimento comune.
 */
public class Goblin extends AbstractEnemy {

    /**
     * Costruisce un Goblin tramite Dependency Injection.
     *
     * @param stats Le statistiche base del Goblin.
     */
    public Goblin(final CombatStats stats) {
        super(stats);
    }
}
