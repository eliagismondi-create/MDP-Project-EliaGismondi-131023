package it.unicam.cs.mpgc.rpg131023.controller;

import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Loot;

import java.util.Map;
import java.util.Objects;

public class GameManager {

    public enum GameState {
        HUB,
        IN_COMBAT,
        GAME_OVER
    }

    private final Hero hero;
    private final Map<String, Dungeon> worldMap;
    private GameState currentState;
    private Dungeon currentDungeon;
    private CombatManager activeCombat;

    public GameManager(final Hero hero, final Map<String, Dungeon> worldMap) {
        this.hero = Objects.requireNonNull(hero, "L'eroe non puo' essere null");
        this.worldMap = Objects.requireNonNull(worldMap, "La worldMap non puo' essere null");
        this.currentState = GameState.HUB;
    }

    public void enterDungeon(final String dungeonId) {
        if (this.currentState != GameState.HUB) {
            throw new IllegalStateException("Lo stato attuale non e' HUB.");
        }

        final Dungeon dungeon = this.worldMap.get(dungeonId);
        if (dungeon == null) {
            throw new IllegalArgumentException("Dungeon inesistente: " + dungeonId);
        }

        this.currentDungeon = dungeon;
        startEncounter();
    }

    private void startEncounter() {
        final EnemyType enemyType = this.currentDungeon.getEnemySpawns().keySet().iterator().next();
        final AbstractEnemy enemy = EnemyFactory.create(enemyType.name());

        this.activeCombat = new CombatManager(this.hero, enemy);
        this.currentState = GameState.IN_COMBAT;
    }

    public void resolveCombatEnd() {
        if (this.activeCombat == null || !this.activeCombat.isCombatOver()) {
            return;
        }

        this.hero.addHunger(25);
        if (!this.hero.isAlive()) {
            this.currentState = GameState.GAME_OVER;
            return;
        }

        if (this.activeCombat.isHeroVictorious()) {
            distributeLoot();
            this.currentState = GameState.HUB;
        } else {
            this.currentState = GameState.GAME_OVER;
        }
    }

    public void retreatFromDungeon() {
        if (this.currentState != GameState.IN_COMBAT) {
            throw new IllegalStateException("Non sei in un dungeon.");
        }
        if (this.activeCombat != null && this.activeCombat.hasCombatStarted()) {
            throw new IllegalStateException("Il combattimento e' gia' iniziato, non puoi fuggire.");
        }
        
        this.hero.addHunger(25);
        if (!this.hero.isAlive()) {
            this.currentState = GameState.GAME_OVER;
        } else {
            this.currentState = GameState.HUB;
            this.activeCombat = null;
            this.currentDungeon = null;
        }
    }

    private void distributeLoot() {
        if (this.currentDungeon != null) {
            for (Loot loot : this.currentDungeon.getTreasures()) {
                loot.applyTo(this.hero);
            }
        }
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public CombatManager getActiveCombat() {
        return this.activeCombat;
    }

    public Dungeon getCurrentDungeon() {
        return this.currentDungeon;
    }

    public Hero getHero() {
        return this.hero;
    }
}
