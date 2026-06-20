package it.unicam.cs.mpgc.rpg131023.model.dungeon;

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
     * @param dungeonId Unique level identifier.
     * @param dto       Parsed data transfer object.
     * @return Fully populated dungeon instance.
     */
    public static Dungeon createDungeon(String dungeonId, DungeonDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DungeonDTO cannot be null for ID: " + dungeonId);
        }

        final Dungeon dungeon = new Dungeon(dungeonId, dto.name, dto.description);

        if (dto.loot != null) {
            dto.loot.forEach((type, amount) -> dungeon.addLoot(new ResourceLoot(type, amount)));
        }

        if (dto.enemySpawns != null) {
            dto.enemySpawns.forEach(dungeon::addEnemySpawn);
        }

        return dungeon;
    }
}
