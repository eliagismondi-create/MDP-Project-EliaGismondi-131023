package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

/**
 * Represents a loot item that can be collected by the hero.
 */
public interface Loot {
    /**
     * Applies the loot effect to the hero.
     * 
     * @param hero The hero receiving the loot.
     */
    void applyTo(AbstractHero hero);

    /**
     * Accepts a loot visitor.
     * 
     * @param visitor The visitor to accept.
     */
    void accept(LootVisitor visitor);
}
