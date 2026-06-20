package it.unicam.cs.mpgc.rpg131023.model.player;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

/**
 * Concrete implementation of AbstractHero.
 */
public class Warrior extends AbstractHero {
    public Warrior(final CombatStats stats) {
        super(stats);
    }
}
