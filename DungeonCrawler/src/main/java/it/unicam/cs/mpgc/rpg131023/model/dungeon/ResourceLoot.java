package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

/**
 * Represents loot that provides collectible resources.
 */
public class ResourceLoot implements Loot {
    private final ResourceType type;
    private final int amount;

    public ResourceLoot(ResourceType type, int amount) {
        if (type == null) {
            throw new NullPointerException("Resource type cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void applyTo(AbstractHero hero) {
        hero.addResource(this.type, this.amount);
    }

    @Override
    public void accept(LootVisitor visitor) {
        visitor.visit(this);
    }

    public ResourceType getType() {
        return this.type;
    }

    public int getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLoot that = (ResourceLoot) o;
        return amount == that.amount && type == that.type;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, amount);
    }
}
