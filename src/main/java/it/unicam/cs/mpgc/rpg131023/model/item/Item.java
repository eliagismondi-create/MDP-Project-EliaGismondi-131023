package it.unicam.cs.mpgc.rpg131023.model.item;

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
     * Applies the item's effects to the consumer.
     *
     * @param consumer The entity using the item.
     */
    void use(ItemConsumer consumer);
}
