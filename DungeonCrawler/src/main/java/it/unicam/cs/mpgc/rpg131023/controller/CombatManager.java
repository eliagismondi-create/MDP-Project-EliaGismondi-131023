package it.unicam.cs.mpgc.rpg131023.controller;

import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

import java.util.Objects;

public class CombatManager {
    private final AbstractHero hero;
    private final AbstractEnemy enemy;
    private boolean heroTurn;
    private boolean combatStarted;

    public CombatManager(final AbstractHero hero, final AbstractEnemy enemy) {
        this.hero = Objects.requireNonNull(hero, "Hero cannot be null");
        this.enemy = Objects.requireNonNull(enemy, "Enemy cannot be null");
        this.heroTurn = true;
        this.combatStarted = false;
    }

    public void executeNextTurn() {
        this.combatStarted = true;
        if (isCombatOver()) {
            return;
        }

        if (this.heroTurn) {
            this.hero.attack(this.enemy);
        } else {
            this.enemy.attack(this.hero);
        }

        this.heroTurn = !this.heroTurn;
    }

    public boolean isCombatOver() {
        return !this.hero.isAlive() || !this.enemy.isAlive();
    }

    public boolean isHeroVictorious() {
        return isCombatOver() && this.hero.isAlive();
    }

    public boolean hasCombatStarted() {
        return this.combatStarted;
    }

    public AbstractHero getHero() {
        return this.hero;
    }

    public AbstractEnemy getEnemy() {
        return this.enemy;
    }
}
