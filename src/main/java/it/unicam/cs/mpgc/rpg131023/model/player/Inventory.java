package it.unicam.cs.mpgc.rpg131023.model.player;

import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.item.Item;

/**
 * Manages the collection of items for the hero.
 * Emits property change events to keep the UI synchronized.
 */
public class Inventory {
    private final Map<Item, Integer> items;
    private final PropertyChangeSupport support;

    public Inventory(PropertyChangeSupport support) {
        this.items = new HashMap<>();
        this.support = support;
    }

    public void addItem(Item item, int amount) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be greater than zero.");
        }
        this.items.put(item, this.items.getOrDefault(item, 0) + amount);
        support.firePropertyChange("inventory", null, getItems());
    }

    public boolean consumeItem(Item item, int amount) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to consume must be greater than zero.");
        }

        final int currentAmount = this.items.getOrDefault(item, 0);
        if (currentAmount < amount) {
            return false;
        }

        this.items.put(item, currentAmount - amount);
        support.firePropertyChange("inventory", null, getItems());
        return true;
    }

    public Map<Item, Integer> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    public void clear() {
        this.items.clear();
        support.firePropertyChange("inventory", null, getItems());
    }

    public void setItemForce(Item item, int amount) {
        this.items.put(item, amount);
        support.firePropertyChange("inventory", null, getItems());
    }
}
