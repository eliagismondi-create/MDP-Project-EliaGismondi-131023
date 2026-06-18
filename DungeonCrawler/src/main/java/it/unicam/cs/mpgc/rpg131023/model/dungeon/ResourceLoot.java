package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

/**
 * Rappresenta un tipo di bottino che fornisce risorse collezionabili.
 */
public class ResourceLoot implements Loot {
    private final ResourceType type;
    private final int amount;

    public ResourceLoot(ResourceType type, int amount) {
        if (type == null) {
            throw new NullPointerException("Il tipo di risorsa non puo' essere null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' deve essere maggiore di zero.");
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
