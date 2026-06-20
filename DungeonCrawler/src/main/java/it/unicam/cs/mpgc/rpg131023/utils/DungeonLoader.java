package it.unicam.cs.mpgc.rpg131023.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Utility for dynamically parsing Dungeons from JSON streams.
 * Isolates parsing logic from the Dungeon domain class.
 */
public final class DungeonLoader {

    private DungeonLoader() {
        // Prevents instantiation
    }

    /**
     * Parses the JSON stream and returns a map of DungeonDTOs.
     *
     * @param reader The Reader providing the JSON data.
     * @return A map of all parsed dungeons, indexed by ID.
     */
    public static Map<String, DungeonDTO> parseDungeons(Reader reader) {
        try {
            final Gson gson = new Gson();
            final Type type = new TypeToken<Map<String, DungeonDTO>>() {
            }.getType();
            Map<String, DungeonDTO> parsedMap = gson.fromJson(reader, type);

            if (parsedMap == null || parsedMap.isEmpty()) {
                throw new IllegalStateException("Dungeons JSON stream is empty or malformed.");
            }

            return parsedMap;
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Critical error parsing dungeons JSON", e);
        }
    }
}
