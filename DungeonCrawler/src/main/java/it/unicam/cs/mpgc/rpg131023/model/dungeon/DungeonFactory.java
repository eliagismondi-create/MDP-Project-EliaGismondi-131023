package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import it.unicam.cs.mpgc.rpg131023.utils.DungeonDTO;

/**
 * Factory responsible for assembling Dungeon domain objects from DTOs.
 */
public final class DungeonFactory {

    private DungeonFactory() {
        // Prevents instantiation
    }

    /**
     * Creates a fully populated Dungeon instance based on a DTO.
     *
     * @param dungeonId The textual identifier of the dungeon.
     * @param dto       The parsed data transfer object.
     * @return A new instance of the Dungeon.
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
