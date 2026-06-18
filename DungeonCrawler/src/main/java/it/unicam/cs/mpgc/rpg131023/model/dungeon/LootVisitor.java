package it.unicam.cs.mpgc.rpg131023.model.dungeon;

/**
 * Interfaccia per il pattern Visitor applicato al Loot.
 * Permette di definire nuove operazioni (es. rendering visivo) sui Loot 
 * senza modificarne le classi concrete (OCP).
 */
public interface LootVisitor {
    /**
     * Visita un ResourceLoot.
     * @param loot L'oggetto ResourceLoot da visitare.
     */
    void visit(ResourceLoot loot);
}
