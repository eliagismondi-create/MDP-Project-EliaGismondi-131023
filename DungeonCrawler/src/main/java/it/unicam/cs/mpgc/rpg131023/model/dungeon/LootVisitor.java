package it.unicam.cs.mpgc.rpg131023.model.dungeon;

/**
 * Visitor interface for loot items.
 */
public interface LootVisitor {
    /**
     * Visits a resource loot.
     * @param loot The resource loot to visit.
     */
    void visit(ResourceLoot loot);
}
