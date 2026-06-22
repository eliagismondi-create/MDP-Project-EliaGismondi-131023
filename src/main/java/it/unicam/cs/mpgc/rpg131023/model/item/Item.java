package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

/**
 * Interface representing an item in the game.
 * Replaces the previous ResourceType enum using the Strategy pattern.
 */
public interface Item {
    
    /**
     * @return The unique identifier of the item.
     */
    String getId();

    /**
     * @return The display name of the item.
     */
    String getName();

    /**
     * Applies the item's effects to the hero.
     *
     * @param hero The hero using the item.
     */
    void use(AbstractHero hero);
}
