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

/**
 * Core controller that manages the overall game state, dungeon exploration, and combat.
 */
public class GameManager {

    public static final int COMBAT_WIN_XP_REWARD = 25;

    private final EventDispatcher eventDispatcher = new EventDispatcher();

    private final AbstractHero hero;
    private final Map<String, Dungeon> worldMap;
    private final EnemyFactory enemyFactory;
    private GameState currentState = new HubState();
    private Dungeon currentDungeon;
    private CombatManager<AbstractHero, Enemy> activeCombat;

    /**
     * Constructs a new GameManager.
     *
     * @param hero         the player character
     * @param worldMap     the map containing all available dungeons
     * @param enemyFactory the factory used to create enemies
     */
    public GameManager(final AbstractHero hero, final Map<String, Dungeon> worldMap, final EnemyFactory enemyFactory) {
        this.hero = Objects.requireNonNull(hero, "Hero cannot be null");
        this.worldMap = Objects.requireNonNull(worldMap, "WorldMap cannot be null");
        this.enemyFactory = Objects.requireNonNull(enemyFactory, "EnemyFactory cannot be null");
    }

    /**
     * Gets the event dispatcher used to notify property changes and log events.
     *
     * @return the event dispatcher
     */
    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Changes the current game state and fires a property change event.
     *
     * @param newState the new game state
     */
    public void changeState(GameState newState) {
        GameState oldState = this.currentState;
        this.currentState = newState;
        eventDispatcher.firePropertyChange("currentState", oldState, this.currentState);
    }

    /**
     * Sets the current dungeon and fires a property change event.
     *
     * @param dungeon the dungeon to set as current
     */
    public void setCurrentDungeon(Dungeon dungeon) {
        Dungeon oldDungeon = this.currentDungeon;
        this.currentDungeon = dungeon;
        eventDispatcher.firePropertyChange("currentDungeon", oldDungeon, this.currentDungeon);
    }

    /**
     * Sets the active combat manager and fires a property change event.
     *
     * @param combatManager the new active combat manager
     */
    public void setActiveCombat(CombatManager<AbstractHero, Enemy> combatManager) {
        CombatManager<AbstractHero, Enemy> oldCombat = this.activeCombat;
        this.activeCombat = combatManager;
        eventDispatcher.firePropertyChange("activeCombat", oldCombat, this.activeCombat);
    }

    /**
     * Initiates the entry into a specified dungeon, delegating to the current state.
     *
     * @param dungeonId the identifier of the dungeon to enter
     */
    public void enterDungeon(final String dungeonId) {
        this.currentState.enterDungeon(this, dungeonId);
    }

    /**
     * Starts a new combat encounter by creating an enemy and an active combat manager.
     */
    public void startEncounter() {
        final String enemyType = this.currentDungeon.getNextEnemyType();
        final Enemy enemy = this.enemyFactory.create(enemyType);

        setActiveCombat(new CombatManager<>(this.hero, enemy));
    }

    /**
     * Applies fatigue to the hero and updates their health status accordingly.
     */
    public void applyFatigueAndCheckDeath() {
        this.hero.sufferFatigue();
    }

    /**
     * Resolves the end of the current combat, delegating to the current state.
     */
    public void resolveCombatEnd() {
        this.currentState.resolveCombatEnd(this);
    }

    /**
     * Retreats the hero from the current dungeon, delegating to the current state.
     */
    public void retreatFromDungeon() {
        this.currentState.retreatFromDungeon(this);
    }

    /**
     * Gets the current game state.
     *
     * @return the current game state
     */
    public GameState getCurrentState() {
        return this.currentState;
    }

    /**
     * Gets the active combat manager.
     *
     * @return the active combat manager
     */
    public CombatManager<AbstractHero, Enemy> getActiveCombat() {
        return this.activeCombat;
    }

    /**
     * Gets the currently explored dungeon.
     *
     * @return the current dungeon
     */
    public Dungeon getCurrentDungeon() {
        return this.currentDungeon;
    }

    /**
     * Gets the player's hero.
     *
     * @return the hero
     */
    public AbstractHero getHero() {
        return this.hero;
    }

    /**
     * Gets an unmodifiable view of the world map.
     *
     * @return the world map
     */
    public Map<String, Dungeon> getWorldMap() {
        return Collections.unmodifiableMap(this.worldMap);
    }

    /**
     * Logs an event message using the event dispatcher.
     *
     * @param message the message to log
     */
    public void logEvent(String message) {
        eventDispatcher.logEvent(message);
    }

    /**
     * Executes a full combat round: hero attacks, then enemy counterattacks.
     * Logs damage dealt and resolves the combat if it ends.
     * This method centralizes combat flow logic that was previously in the View.
     */
    public void executeCombatRound() {
        CombatManager<AbstractHero, Enemy> cm = this.activeCombat;
        if (cm == null || cm.isCombatOver()) {
            return;
        }

        executeHeroTurn(cm);
        if (!cm.isCombatOver()) {
            executeEnemyTurn(cm);
        }

        if (cm.isCombatOver()) {
            logCombatOutcome(cm);
            resolveCombatEnd();
        }
    }

    private void executeHeroTurn(CombatManager<AbstractHero, Enemy> cm) {
        cm.executeNextTurn();
        logEvent("You land a hit! Dealt " + cm.getLastDamageDealt() + " damage.");
    }

    private void executeEnemyTurn(CombatManager<AbstractHero, Enemy> cm) {
        cm.executeNextTurn();
        logEvent("The enemy counterattacks. Received " + cm.getLastDamageDealt() + " damage.");
    }

    private void logCombatOutcome(CombatManager<AbstractHero, Enemy> cm) {
        if (cm.isHeroVictorious()) {
            logEvent("VICTORY! Enemy defeated.");
        } else {
            logEvent("DEFEAT! You have fallen in battle.");
        }
    }
}
