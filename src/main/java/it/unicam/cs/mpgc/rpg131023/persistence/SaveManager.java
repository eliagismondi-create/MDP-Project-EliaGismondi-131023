package it.unicam.cs.mpgc.rpg131023.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

import java.io.IOException;

/**
 * Handles serializing and deserializing game state using JSON.
 * Delegates actual file I/O to a StorageService.
 */
public class SaveManager {

    private final StorageService storageService;
    private final String saveFilePath;
    private final Gson gson;

    /**
     * Constructs a SaveManager with the specified storage service and file path.
     *
     * @param storageService The service responsible for read/write operations.
     * @param saveFilePath   The destination path for the save file.
     */
    public SaveManager(StorageService storageService, String saveFilePath) {
        this.storageService = storageService;
        this.saveFilePath = saveFilePath;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Serializes the hero's state and saves it.
     *
     * @param hero The hero to save.
     */
    public void saveGame(AbstractHero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot save a null hero.");
        }

        HeroSaveDTO dto = HeroSaveDTO.fromHero(hero);
        try {
            String json = gson.toJson(dto);
            storageService.write(saveFilePath, json);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save the game to " + saveFilePath, e);
        }
    }

    /**
     * Reads the save file and returns the deserialized hero data.
     *
     * @return The loaded HeroSaveDTO.
     */
    public HeroSaveDTO loadGame() {
        try {
            String json = storageService.read(saveFilePath);
            HeroSaveDTO dto = gson.fromJson(json, HeroSaveDTO.class);
            if (dto == null) {
                throw new IllegalStateException("Save file is corrupted or empty.");
            }
            return dto;
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Failed to load the game from " + saveFilePath, e);
        }
    }
}
