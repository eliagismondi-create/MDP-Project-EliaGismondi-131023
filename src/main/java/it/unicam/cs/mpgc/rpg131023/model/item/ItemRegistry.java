package it.unicam.cs.mpgc.rpg131023.model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight factory for Item instances.
 */
public class ItemRegistry {
    private static final Map<String, Item> REGISTRY = new HashMap<>();

    /**
     * Initializes the registry with default items.
     * Call this at application startup.
     */
    public static void init() {
        register(new HealthPotion());
        register(new Food());
        register(new Armor());
        register(new Sword());
    }

    public static void register(Item item) {
        REGISTRY.put(item.getId(), item);
    }

    /**
     * Retrieves a flyweight instance of an item by its ID.
     *
     * @param id The item ID.
     * @return The flyweight item instance, or null if not found.
     */
    public static Item get(String id) {
        return REGISTRY.get(id);
    }
}
