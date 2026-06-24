package it.unicam.cs.mpgc.rpg131023.controller.state;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;

/**
 * Base contract for the state machine pattern managing the game loop.
 * Defines how actions behave depending on what the player is currently doing.
 */
public interface GameState {
    /**
     * Attempts to start a run in the given dungeon.
     */
    void enterDungeon(GameManager context, String dungeonId);

    /**
     * Attempts to flee back to safety. May apply penalties or be blocked.
     */
    void retreatFromDungeon(GameManager context);

    /**
     * Handles cleanup after a fight finishes (xp, loot, death checks).
     */
    void resolveCombatEnd(GameManager context);

    StateType getType();
}
