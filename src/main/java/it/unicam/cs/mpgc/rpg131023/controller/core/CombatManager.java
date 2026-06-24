package it.unicam.cs.mpgc.rpg131023.controller.core;

import it.unicam.cs.mpgc.rpg131023.model.combat.Combatant;

import java.util.Objects;

/**
 * Manages the combat sequence between two combatants.
 *
 * @param <F1> the type of the first combatant (usually the hero)
 * @param <F2> the type of the second combatant (usually the enemy)
 */
public class CombatManager<F1 extends Combatant, F2 extends Combatant> {
    private final F1 fighter1;
    private final F2 fighter2;
    private boolean isFighter1Turn;
    private boolean combatStarted;

    /**
     * Constructs a new CombatManager with the specified fighters.
     *
     * @param fighter1 the first combatant
     * @param fighter2 the second combatant
     */
    public CombatManager(final F1 fighter1, final F2 fighter2) {
        this.fighter1 = Objects.requireNonNull(fighter1, "Fighter 1 cannot be null");
        this.fighter2 = Objects.requireNonNull(fighter2, "Fighter 2 cannot be null");
        this.isFighter1Turn = true;
        this.combatStarted = false;
    }

    /**
     * Executes the next turn in the combat, allowing the active fighter to attack.
     */
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

    /**
     * Checks if the combat has ended (i.e., at least one combatant is defeated).
     *
     * @return true if the combat is over, false otherwise
     */
    public boolean isCombatOver() {
        return !this.fighter1.isAlive() || !this.fighter2.isAlive();
    }

    /**
     * Checks if the first fighter (hero) won the combat.
     *
     * @return true if the hero is alive and the combat is over, false otherwise
     */
    public boolean isHeroVictorious() {
        return isCombatOver() && this.fighter1.isAlive();
    }

    /**
     * Checks if the combat has already started.
     *
     * @return true if the combat has started, false otherwise
     */
    public boolean hasCombatStarted() {
        return this.combatStarted;
    }

    /**
     * Gets the first combatant (hero).
     *
     * @return the first combatant
     */
    public F1 getHero() {
        return this.fighter1;
    }

    /**
     * Gets the second combatant (enemy).
     *
     * @return the second combatant
     */
    public F2 getEnemy() {
        return this.fighter2;
    }
}
