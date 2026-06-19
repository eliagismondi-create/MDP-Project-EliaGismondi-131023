package it.unicam.cs.mpgc.rpg131023.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unicam.cs.mpgc.rpg131023.model.player.Hero;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveManager {

    private static final String SAVE_DIR = System.getProperty("user.home") + File.separator + ".dungeoncrawler";
    private static final String SAVE_FILE = SAVE_DIR + File.separator + "savegame.json";

    private SaveManager() {
        // Impedisce l'istanza
    }

    public static void saveGame(Hero hero) {
        if (hero == null) {
            throw new IllegalArgumentException("Cannot save a null hero.");
        }

        File directory = new File(SAVE_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        HeroSaveDTO dto = HeroSaveDTO.fromHero(hero);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            gson.toJson(dto, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save the game to " + SAVE_FILE, e);
        }
    }

    public static HeroSaveDTO loadGame() {
        File saveFile = new File(SAVE_FILE);
        if (!saveFile.exists()) {
            throw new IllegalStateException("No save game found at " + SAVE_FILE);
        }

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(saveFile)) {
            HeroSaveDTO dto = gson.fromJson(reader, HeroSaveDTO.class);
            if (dto == null) {
                throw new IllegalStateException("Save file is corrupted or empty.");
            }
            return dto;
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            throw new IllegalStateException("Failed to load the game from " + SAVE_FILE, e);
        }
    }
}
