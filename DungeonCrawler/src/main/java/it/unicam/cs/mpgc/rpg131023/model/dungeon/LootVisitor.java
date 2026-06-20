package it.unicam.cs.mpgc.rpg131023.model.dungeon;

/**
 * Dispatcher pattern interface for loot variants.
 */
public interface LootVisitor {
    /**
     * Handles resource-based loot logic.
     * @param loot The resource loot instance.
     */
    void visit(ResourceLoot loot);
}
