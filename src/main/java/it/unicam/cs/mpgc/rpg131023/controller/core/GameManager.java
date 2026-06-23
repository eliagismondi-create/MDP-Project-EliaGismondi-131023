package it.unicam.cs.mpgc.rpg131023.controller.core;

import it.unicam.cs.mpgc.rpg131023.controller.events.EventDispatcher;
import it.unicam.cs.mpgc.rpg131023.controller.state.GameState;
import it.unicam.cs.mpgc.rpg131023.controller.state.HubState;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyFactory;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class GameManager {

    public static final int COMBAT_WIN_XP_REWARD = 25;

    private final EventDispatcher eventDispatcher = new EventDispatcher();

    private final AbstractHero hero;
    private final Map<String, Dungeon> worldMap;
    private final EnemyFactory enemyFactory;
    private GameState currentState = new HubState();
    private Dungeon currentDungeon;
    private CombatManager<AbstractHero, Enemy> activeCombat;

    public GameManager(final AbstractHero hero, final Map<String, Dungeon> worldMap, final EnemyFactory enemyFactory) {
        this.hero = Objects.requireNonNull(hero, "Hero cannot be null");
        this.worldMap = Objects.requireNonNull(worldMap, "WorldMap cannot be null");
        this.enemyFactory = Objects.requireNonNull(enemyFactory, "EnemyFactory cannot be null");
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    public void changeState(GameState newState) {
        GameState oldState = this.currentState;
        this.currentState = newState;
        eventDispatcher.firePropertyChange("currentState", oldState, this.currentState);
    }

    public void setCurrentDungeon(Dungeon dungeon) {
        Dungeon oldDungeon = this.currentDungeon;
        this.currentDungeon = dungeon;
        eventDispatcher.firePropertyChange("currentDungeon", oldDungeon, this.currentDungeon);
    }

    public void setActiveCombat(CombatManager<AbstractHero, Enemy> combatManager) {
        CombatManager<AbstractHero, Enemy> oldCombat = this.activeCombat;
        this.activeCombat = combatManager;
        eventDispatcher.firePropertyChange("activeCombat", oldCombat, this.activeCombat);
    }

    public void enterDungeon(final String dungeonId) {
        this.currentState.enterDungeon(this, dungeonId);
    }

    public void startEncounter() {
        final String enemyType = this.currentDungeon.getNextEnemyType();
        final Enemy enemy = this.enemyFactory.create(enemyType);

        setActiveCombat(new CombatManager<>(this.hero, enemy));
    }

    public void applyFatigueAndCheckDeath() {
        this.hero.sufferFatigue();
    }

    public void resolveCombatEnd() {
        this.currentState.resolveCombatEnd(this);
    }

    public void retreatFromDungeon() {
        this.currentState.retreatFromDungeon(this);
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public CombatManager<AbstractHero, Enemy> getActiveCombat() {
        return this.activeCombat;
    }

    public Dungeon getCurrentDungeon() {
        return this.currentDungeon;
    }

    public AbstractHero getHero() {
        return this.hero;
    }

    public Map<String, Dungeon> getWorldMap() {
        return Collections.unmodifiableMap(this.worldMap);
    }

    public void logEvent(String message) {
        eventDispatcher.logEvent(message);
    }
}
