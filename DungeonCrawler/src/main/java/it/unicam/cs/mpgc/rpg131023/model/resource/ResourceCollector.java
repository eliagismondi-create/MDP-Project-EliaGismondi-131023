package it.unicam.cs.mpgc.rpg131023.model.resource;

/**
 * Interface representing an entity capable of collecting resources.
 */
public interface ResourceCollector {

    /**
     * Stores specific resource quantity in inventory.
     *
     * @param type   Resource variant.
     * @param amount Quantity to add.
     */
    void addResource(ResourceType type, int amount);
}
