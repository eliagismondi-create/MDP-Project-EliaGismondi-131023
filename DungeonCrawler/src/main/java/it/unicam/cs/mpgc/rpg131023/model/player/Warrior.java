package it.unicam.cs.mpgc.rpg131023.model.player;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

/**
 * Default concrete implementation of a playable hero.
 * Inherits core combat and inventory mechanics without custom logic.
 */
public class Warrior extends AbstractHero {
    public Warrior(final CombatStats stats) {
        super(stats);
    }
}
