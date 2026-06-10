package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.CombatStats;

public abstract class AbstractEnemy extends AbstractCombatant {

    public AbstractEnemy(final CombatStats stats) {
        super(stats);
    }

}
