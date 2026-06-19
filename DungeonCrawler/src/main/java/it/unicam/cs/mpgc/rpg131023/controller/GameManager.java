package it.unicam.cs.mpgc.rpg131023.controller;

import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Loot;

import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GameManager {

    public enum GameState {
        HUB,
        IN_COMBAT,
        GAME_OVER
    }

    private final Hero hero;
    private final Map<String, Dungeon> worldMap;
    private final ObjectProperty<GameState> currentState = new SimpleObjectProperty<>(GameState.HUB);
    private final ObjectProperty<Dungeon> currentDungeon = new SimpleObjectProperty<>();
    private final ObjectProperty<CombatManager> activeCombat = new SimpleObjectProperty<>();
    private final ObservableList<String> eventLog = FXCollections.observableArrayList();

    public GameManager(final Hero hero, final Map<String, Dungeon> worldMap) {
        this.hero = Objects.requireNonNull(hero, "L'eroe non puo' essere null");
        this.worldMap = Objects.requireNonNull(worldMap, "La worldMap non puo' essere null");
        this.currentState.set(GameState.HUB);
    }

    public void enterDungeon(final String dungeonId) {
        if (this.currentState.get() != GameState.HUB) {
            throw new IllegalStateException("Lo stato attuale non e' HUB.");
        }

        final Dungeon dungeon = this.worldMap.get(dungeonId);
        if (dungeon == null) {
            throw new IllegalArgumentException("Dungeon inesistente: " + dungeonId);
        }

        this.currentDungeon.set(dungeon);
        startEncounter();
    }

    private void startEncounter() {
        final EnemyType enemyType = this.currentDungeon.get().getEnemySpawns().keySet().iterator().next();
        final AbstractEnemy enemy = EnemyFactory.create(enemyType.name());

        this.activeCombat.set(new CombatManager(this.hero, enemy));
        this.currentState.set(GameState.IN_COMBAT);
    }

    public void resolveCombatEnd() {
        if (this.activeCombat.get() == null || !this.activeCombat.get().isCombatOver()) {
            return;
        }

        this.hero.sufferFatigue();
        if (!this.hero.isAlive()) {
            this.currentState.set(GameState.GAME_OVER);
            return;
        }

        if (this.activeCombat.get().isHeroVictorious()) {
            if (this.currentDungeon.get() != null) {
                this.currentDungeon.get().claimLoot(this.hero);
            }
            this.currentState.set(GameState.HUB);
        } else {
            this.currentState.set(GameState.GAME_OVER);
        }
    }

    public void retreatFromDungeon() {
        if (this.currentState.get() != GameState.IN_COMBAT) {
            throw new IllegalStateException("Non sei in un dungeon.");
        }
        if (this.activeCombat.get() != null && this.activeCombat.get().hasCombatStarted()) {
            throw new IllegalStateException("Il combattimento e' gia' iniziato, non puoi fuggire.");
        }
        
        this.hero.sufferFatigue();
        if (!this.hero.isAlive()) {
            this.currentState.set(GameState.GAME_OVER);
        } else {
            this.currentState.set(GameState.HUB);
            this.activeCombat.set(null);
            this.currentDungeon.set(null);
        }
    }



    public GameState getCurrentState() {
        return this.currentState.get();
    }

    public ObjectProperty<GameState> currentStateProperty() {
        return this.currentState;
    }

    public CombatManager getActiveCombat() {
        return this.activeCombat.get();
    }

    public ObjectProperty<CombatManager> activeCombatProperty() {
        return this.activeCombat;
    }

    public Dungeon getCurrentDungeon() {
        return this.currentDungeon.get();
    }

    public ObjectProperty<Dungeon> currentDungeonProperty() {
        return this.currentDungeon;
    }

    public Hero getHero() {
        return this.hero;
    }

    public ObservableList<String> getEventLog() {
        return this.eventLog;
    }

    public void logEvent(String message) {
        this.eventLog.add("· " + message);
    }
}
