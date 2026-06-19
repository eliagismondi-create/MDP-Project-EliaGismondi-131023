package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
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
    public void applyTo(Hero hero) {
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
}
