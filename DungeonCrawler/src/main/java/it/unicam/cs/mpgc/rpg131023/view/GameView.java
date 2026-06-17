package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.CombatManager;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;

import java.util.Map;

/**
 * Interfaccia astratta per la View, che garantisce l'indipendenza della
 * logica di gioco dalla tecnologia di presentazione (Console o JavaFX).
 */
public interface GameView {
    /**
     * Mostra un messaggio di benvenuto.
     */
    void showWelcomeMessage();

    /**
     * Aggiorna la vista per mostrare l'HUB.
     * 
     * @param hero     L'eroe con le sue statistiche attuali.
     * @param worldMap I dungeon disponibili per l'esplorazione.
     */
    void displayHub(Hero hero, Map<String, Dungeon> worldMap);

    /**
     * Aggiorna la vista per mostrare l'arena di combattimento.
     * 
     * @param combatManager Il manager del combattimento corrente.
     */
    void displayCombat(CombatManager combatManager);

    /**
     * Mostra la schermata di Game Over (eroe morto).
     */
    void displayGameOver();

    /**
     * Aggiunge un messaggio al log degli eventi.
     * 
     * @param message Il messaggio da mostrare.
     */
    void showMessage(String message);
}
