package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.core.CombatManager;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

import java.util.Map;

/**
 * UI contract. Keeps domain logic decoupled from specific presentation frameworks.
 */
public interface GameView {
    void showWelcomeMessage();
    void displayHub(AbstractHero hero, Map<String, Dungeon> worldMap);
    void displayCombat(CombatManager<AbstractHero, it.unicam.cs.mpgc.rpg131023.model.enemy.Enemy> combatManager);
    void displayGameOver();
    void showMessage(String message);
}
