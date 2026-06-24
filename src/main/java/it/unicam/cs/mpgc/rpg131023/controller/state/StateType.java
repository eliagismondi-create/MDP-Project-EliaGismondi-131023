package it.unicam.cs.mpgc.rpg131023.controller.state;

/**
 * Identifiers for the core game loop states.
 */
public enum StateType {
    /** Safe in town, ready to pick a dungeon. */
    HUB,
    /** Actively fighting an enemy. */
    IN_COMBAT,
    /** The hero died. */
    GAME_OVER
}
