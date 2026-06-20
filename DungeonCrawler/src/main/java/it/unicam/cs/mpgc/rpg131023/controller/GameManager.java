package it.unicam.cs.mpgc.rpg131023.controller;

import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameManager {

    public enum GameState {
        HUB,
        IN_COMBAT,
        GAME_OVER
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private final AbstractHero hero;
    private final Map<String, Dungeon> worldMap;
    private GameState currentState = GameState.HUB;
    private Dungeon currentDungeon;
    private CombatManager activeCombat;
    private final List<String> eventLog = new ArrayList<>();

    public GameManager(final AbstractHero hero, final Map<String, Dungeon> worldMap) {
        this.hero = Objects.requireNonNull(hero, "Hero cannot be null");
        this.worldMap = Objects.requireNonNull(worldMap, "WorldMap cannot be null");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    private void setCurrentState(GameState newState) {
        GameState oldState = this.currentState;
        this.currentState = newState;
        support.firePropertyChange("currentState", oldState, this.currentState);
    }

    private void setCurrentDungeon(Dungeon dungeon) {
        Dungeon oldDungeon = this.currentDungeon;
        this.currentDungeon = dungeon;
        support.firePropertyChange("currentDungeon", oldDungeon, this.currentDungeon);
    }

    private void setActiveCombat(CombatManager combatManager) {
        CombatManager oldCombat = this.activeCombat;
        this.activeCombat = combatManager;
        support.firePropertyChange("activeCombat", oldCombat, this.activeCombat);
    }

    public void enterDungeon(final String dungeonId) {
        if (this.currentState != GameState.HUB) {
            throw new IllegalStateException("Current state is not HUB.");
        }

        final Dungeon dungeon = this.worldMap.get(dungeonId);
        if (dungeon == null) {
            throw new IllegalArgumentException("Dungeon does not exist: " + dungeonId);
        }

        setCurrentDungeon(dungeon);
        startEncounter();
    }

    private void startEncounter() {
        final EnemyType enemyType = this.currentDungeon.getEnemySpawns().keySet().iterator().next();
        final AbstractEnemy enemy = EnemyFactory.create(enemyType.name());

        setActiveCombat(new CombatManager(this.hero, enemy));
        setCurrentState(GameState.IN_COMBAT);
    }

    public void resolveCombatEnd() {
        if (this.activeCombat == null || !this.activeCombat.isCombatOver()) {
            return;
        }

        this.hero.sufferFatigue();
        if (!this.hero.isAlive()) {
            setCurrentState(GameState.GAME_OVER);
            return;
        }

        if (this.activeCombat.isHeroVictorious()) {
            if (this.currentDungeon != null) {
                this.currentDungeon.claimLoot(this.hero);
            }
            setCurrentState(GameState.HUB);
        } else {
            setCurrentState(GameState.GAME_OVER);
        }
    }

    public void retreatFromDungeon() {
        if (this.currentState != GameState.IN_COMBAT) {
            throw new IllegalStateException("You are not in a dungeon.");
        }
        if (this.activeCombat != null && this.activeCombat.hasCombatStarted()) {
            throw new IllegalStateException("Combat has already started, you cannot flee.");
        }
        
        this.hero.sufferFatigue();
        if (!this.hero.isAlive()) {
            setCurrentState(GameState.GAME_OVER);
        } else {
            setCurrentState(GameState.HUB);
            setActiveCombat(null);
            setCurrentDungeon(null);
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

    public AbstractHero getHero() {
        return this.hero;
    }

    public List<String> getEventLog() {
        return Collections.unmodifiableList(this.eventLog);
    }

    public void logEvent(String message) {
        String log = "· " + message;
        this.eventLog.add(log);
        support.firePropertyChange("eventLogAdded", null, log);
    }
}
