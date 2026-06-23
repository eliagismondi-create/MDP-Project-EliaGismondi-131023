package it.unicam.cs.mpgc.rpg131023.controller.state;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;

public class GameOverState implements GameState {

    @Override
    public void enterDungeon(GameManager context, String dungeonId) {
        throw new IllegalStateException("Game Over. You cannot enter dungeons.");
    }

    @Override
    public void retreatFromDungeon(GameManager context) {
        throw new IllegalStateException("Game Over. You cannot retreat.");
    }

    @Override
    public void resolveCombatEnd(GameManager context) {
        // Already game over
    }

    @Override
    public StateType getType() {
        return StateType.GAME_OVER;
    }
}
