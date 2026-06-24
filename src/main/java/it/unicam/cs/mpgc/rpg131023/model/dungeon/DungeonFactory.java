package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonDTO;

/**
 * Assembler for dungeon domain entities from parsed DTOs.
 */
public final class DungeonFactory {

    private DungeonFactory() {
        // Prevents instantiation
    }

    /**
     * Builds a concrete dungeon entity from transfer data.
     *
     * @param dungeonId    Unique level identifier.
     * @param dto          Parsed data transfer object.
     * @param itemRegistry The registry to resolve item IDs for loot.
     * @return Fully populated dungeon instance.
     */
    public static Dungeon createDungeon(String dungeonId, DungeonDTO dto, ItemRegistry itemRegistry) {
        if (dto == null) {
            throw new IllegalArgumentException("DungeonDTO cannot be null for ID: " + dungeonId);
        }

        final Dungeon.Difficulty difficulty = dto.difficulty != null ?
                Dungeon.Difficulty.valueOf(dto.difficulty.toUpperCase()) :
                Dungeon.Difficulty.NORMAL;

        final Dungeon dungeon = new Dungeon(dungeonId, dto.name, dto.description, difficulty);

        if (dto.loot != null) {
            dto.loot.forEach((typeString, amount) -> {
                Item item = itemRegistry.get(typeString);
                dungeon.addLoot(new ResourceLoot(item, amount));
            });
        }

        if (dto.enemySpawns != null) {
            dto.enemySpawns.forEach(dungeon::addEnemySpawn);
        }

        return dungeon;
    }
}
