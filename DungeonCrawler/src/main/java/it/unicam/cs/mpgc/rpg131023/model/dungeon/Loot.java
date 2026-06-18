package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.player.Hero;

/**
 * Interfaccia che astrae il concetto di bottino (Loot).
 * Permette di applicare effetti diversi (Risorse, XP, armi)
 * senza dover modificare le classi consumatrici (GameManager).
 */
public interface Loot {
    /**
     * Applica l'effetto del loot sull'eroe.
     * 
     * @param hero L'eroe che riceve il bottino.
     */
    void applyTo(Hero hero);

    /**
     * Accetta un visitor (Double Dispatch).
     * 
     * @param visitor Il visitor da accettare.
     */
    void accept(LootVisitor visitor);
}
