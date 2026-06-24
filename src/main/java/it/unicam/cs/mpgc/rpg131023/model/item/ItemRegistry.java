package it.unicam.cs.mpgc.rpg131023.model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Injectable registry for Item instances (Flyweight pattern).
 * Unlike the previous static approach, this class is instantiated
 * and injected where needed, following the Dependency Inversion Principle.
 */
public class ItemRegistry {
    private final Map<String, Item> registry = new HashMap<>();

    /**
     * Creates a registry pre-populated with the default game items.
     *
     * @return A ready-to-use ItemRegistry.
     */
    public static ItemRegistry createDefault() {
        ItemRegistry instance = new ItemRegistry();
        instance.register(new HealthPotion());
        instance.register(new Food());
        instance.register(new Armor());
        instance.register(new Sword());
        return instance;
    }

    /**
     * Registers an item in this registry.
     *
     * @param item The item to register.
     */
    public void register(Item item) {
        registry.put(item.getId(), item);
    }

    /**
     * Retrieves a flyweight instance of an item by its ID.
     *
     * @param id The item ID.
     * @return The flyweight item instance.
     * @throws IllegalArgumentException if no item is registered with the given ID.
     */
    public Item get(String id) {
        Item item = registry.get(id);
        if (item == null) {
            throw new IllegalArgumentException("No item registered with ID: " + id);
        }
        return item;
    }
}
