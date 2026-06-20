package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceCollector;

/**
 * Collectible item found within a level.
 */
public interface Loot {
    /**
     * Triggers the loot effect on the character.
     * 
     * @param collector Target character.
     */
    void applyTo(ResourceCollector collector);

    /**
     * Dispatches the visitor to the concrete loot type.
     * 
     * @param visitor The visiting logic instance.
     */
    void accept(LootVisitor visitor);
}
