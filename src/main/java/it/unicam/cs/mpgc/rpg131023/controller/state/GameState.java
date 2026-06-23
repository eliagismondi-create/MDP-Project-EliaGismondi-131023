package it.unicam.cs.mpgc.rpg131023.controller.state;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;

public interface GameState {
    void enterDungeon(GameManager context, String dungeonId);
    void retreatFromDungeon(GameManager context);
    void resolveCombatEnd(GameManager context);
    StateType getType();
}
