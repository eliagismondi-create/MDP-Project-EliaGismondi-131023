package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;

public abstract class AbstractEnemy extends AbstractCombatant {

    public AbstractEnemy(final CombatStats stats) {
        super(stats);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEnemy that = (AbstractEnemy) o;
        return getHealth() == that.getHealth() && getDamage() == that.getDamage();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getHealth(), getDamage(), getClass());
    }
}
