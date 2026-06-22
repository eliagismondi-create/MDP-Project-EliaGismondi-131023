package it.unicam.cs.mpgc.rpg131023.model.resource;

import it.unicam.cs.mpgc.rpg131023.model.item.Item;

/**
 * Interface representing an entity capable of collecting resources.
 */
public interface ResourceCollector {

    /**
     * Stores specific item quantity in inventory.
     *
     * @param item   Item variant.
     * @param amount Quantity to add.
     */
    void addItem(Item item, int amount);
}
