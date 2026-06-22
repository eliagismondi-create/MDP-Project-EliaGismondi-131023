package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceCollector;

/**
 * Loot implementation mapping to game items.
 */
public class ResourceLoot implements Loot {
    private final Item item;
    private final int amount;

    public ResourceLoot(Item item, int amount) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.item = item;
        this.amount = amount;
    }

    @Override
    public void applyTo(ResourceCollector collector) {
        collector.addItem(this.item, this.amount);
    }

    @Override
    public void accept(LootVisitor visitor) {
        visitor.visit(this);
    }

    public Item getItem() {
        return this.item;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLoot that = (ResourceLoot) o;
        return amount == that.amount && item.equals(that.item);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(item, amount);
    }
}
