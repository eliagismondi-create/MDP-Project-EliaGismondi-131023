package it.unicam.cs.mpgc.rpg131023.controller.state;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;

public class CombatState implements GameState {

    @Override
    public void enterDungeon(GameManager context, String dungeonId) {
        throw new IllegalStateException("Current state is not HUB.");
    }

    @Override
    public void retreatFromDungeon(GameManager context) {
        if (context.getActiveCombat() != null && context.getActiveCombat().hasCombatStarted()) {
            throw new IllegalStateException("Combat has already started, you cannot flee.");
        }

        context.applyFatigueAndCheckDeath();
        if (context.getHero().isAlive()) {
            context.setActiveCombat(null);
            context.setCurrentDungeon(null);
            context.changeState(new HubState());
        } else {
            context.changeState(new GameOverState());
        }
    }

    @Override
    public void resolveCombatEnd(GameManager context) {
        if (context.getActiveCombat() == null || !context.getActiveCombat().isCombatOver()) {
            return;
        }

        context.applyFatigueAndCheckDeath();
        if (!context.getHero().isAlive()) {
            context.changeState(new GameOverState());
            return;
        }

        if (context.getActiveCombat().isHeroVictorious()) {
            context.getHero().addXp(GameManager.COMBAT_WIN_XP_REWARD);
            if (context.getCurrentDungeon() != null) {
                context.getCurrentDungeon().claimLoot(context.getHero());
            }
            context.changeState(new HubState());
        } else {
            context.changeState(new GameOverState());
        }
    }

    @Override
    public StateType getType() {
        return StateType.IN_COMBAT;
    }
}
