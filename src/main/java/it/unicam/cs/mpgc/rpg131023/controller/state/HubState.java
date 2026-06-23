package it.unicam.cs.mpgc.rpg131023.controller.state;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;

public class HubState implements GameState {

    @Override
    public void enterDungeon(GameManager context, String dungeonId) {
        final Dungeon dungeon = context.getWorldMap().get(dungeonId);
        if (dungeon == null) {
            throw new IllegalArgumentException("Dungeon does not exist: " + dungeonId);
        }

        context.setCurrentDungeon(dungeon);
        context.startEncounter();
        context.changeState(new CombatState());
    }

    @Override
    public void retreatFromDungeon(GameManager context) {
        throw new IllegalStateException("You are not in a dungeon.");
    }

    @Override
    public void resolveCombatEnd(GameManager context) {
        // Nothing to resolve in Hub
    }

    @Override
    public StateType getType() {
        return StateType.HUB;
    }
}
