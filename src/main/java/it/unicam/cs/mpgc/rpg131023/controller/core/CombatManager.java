package it.unicam.cs.mpgc.rpg131023.controller.core;

import it.unicam.cs.mpgc.rpg131023.model.combat.Combatant;

import java.util.Objects;

public class CombatManager<F1 extends Combatant, F2 extends Combatant> {
    private final F1 fighter1;
    private final F2 fighter2;
    private boolean isFighter1Turn;
    private boolean combatStarted;

    public CombatManager(final F1 fighter1, final F2 fighter2) {
        this.fighter1 = Objects.requireNonNull(fighter1, "Fighter 1 cannot be null");
        this.fighter2 = Objects.requireNonNull(fighter2, "Fighter 2 cannot be null");
        this.isFighter1Turn = true;
        this.combatStarted = false;
    }

    public void executeNextTurn() {
        this.combatStarted = true;
        if (isCombatOver()) {
            return;
        }

        if (this.isFighter1Turn) {
            this.fighter1.attack(this.fighter2);
        } else {
            this.fighter2.attack(this.fighter1);
        }

        this.isFighter1Turn = !this.isFighter1Turn;
    }

    public boolean isCombatOver() {
        return !this.fighter1.isAlive() || !this.fighter2.isAlive();
    }

    public boolean isHeroVictorious() {
        return isCombatOver() && this.fighter1.isAlive();
    }

    public boolean hasCombatStarted() {
        return this.combatStarted;
    }

    public F1 getHero() {
        return this.fighter1;
    }

    public F2 getEnemy() {
        return this.fighter2;
    }
}
