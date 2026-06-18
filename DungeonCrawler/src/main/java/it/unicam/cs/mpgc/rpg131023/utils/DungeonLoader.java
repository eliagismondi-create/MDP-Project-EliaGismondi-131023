package it.unicam.cs.mpgc.rpg131023.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.ResourceLoot;
import it.unicam.cs.mpgc.rpg131023.model.enemy.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

/**
 * Utility per il caricamento dinamico dei Dungeon da file JSON.
 * Rispettando l'SRP, questa classe isola la logica di parsing
 * dalla classe di dominio Dungeon.
 */
public final class DungeonLoader {
    private static final String DUNGEONS_FILE = "/dungeons.json";
    private static Map<String, DungeonDTO> dungeonsCache = null;

    private DungeonLoader() {
        // Impedisce l'istanza
    }

    /**
     * DTO interno per il parsing del JSON.
     */
    private static class DungeonDTO {
        String name;
        String description;
        Map<ResourceType, Integer> loot;
        Map<EnemyType, Integer> enemySpawns;
    }

    /**
     * Carica e restituisce un'istanza di Dungeon in base al suo ID.
     * Utilizza la logica Data-Driven chiamando programmaticamente i metodi sicuri
     * della classe Dungeon.
     * 
     * @param dungeonId L'identificatore testuale del dungeon.
     * @return Una nuova istanza del Dungeon richiesto.
     */
    public static Dungeon loadDungeon(final String dungeonId) {
        if (dungeonsCache == null) {
            loadAllDungeons();
        }

        final DungeonDTO dto = dungeonsCache.get(dungeonId.toLowerCase());
        if (dto == null) {
            throw new IllegalStateException("Nessun dungeon trovato per l'ID: " + dungeonId);
        }

        final Dungeon dungeon = new Dungeon(dungeonId, dto.name, dto.description);

        if (dto.loot != null) {
            for (Map.Entry<ResourceType, Integer> entry : dto.loot.entrySet()) {
                dungeon.addLoot(new ResourceLoot(entry.getKey(), entry.getValue()));
            }
        }

        if (dto.enemySpawns != null) {
            for (Map.Entry<EnemyType, Integer> entry : dto.enemySpawns.entrySet()) {
                dungeon.addEnemySpawn(entry.getKey(), entry.getValue());
            }
        }

        return dungeon;
    }

    /**
     * Carica tutti i dungeon e li restituisce in una mappa.
     * @return Mappa di tutti i dungeon caricati, indicizzati per ID.
     */
    public static Map<String, Dungeon> getAllDungeons() {
        if (dungeonsCache == null) {
            loadAllDungeons();
        }
        final Map<String, Dungeon> worldMap = new java.util.HashMap<>();
        for (String id : dungeonsCache.keySet()) {
            worldMap.put(id, loadDungeon(id));
        }
        return worldMap;
    }

    private static void loadAllDungeons() {
        try (Reader reader = new InputStreamReader(
                Objects.requireNonNull(DungeonLoader.class.getResourceAsStream(DUNGEONS_FILE),
                        "File " + DUNGEONS_FILE + " non trovato nel classpath."))) {

            final Gson gson = new Gson();
            final Type type = new TypeToken<Map<String, DungeonDTO>>() {
            }.getType();
            dungeonsCache = gson.fromJson(reader, type);

            if (dungeonsCache == null || dungeonsCache.isEmpty()) {
                throw new IllegalStateException("Il file dei dungeon e' vuoto o malformato.");
            }
        } catch (java.io.IOException | com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Errore critico durante il caricamento dei dungeon da " + DUNGEONS_FILE, e);
        }
    }
}
