package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.model.player.Warrior;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.persistence.FileStorageService;
import it.unicam.cs.mpgc.rpg131023.persistence.SaveManager;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonDTO;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonLoader;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.DungeonFactory;
import it.unicam.cs.mpgc.rpg131023.utils.StatsLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Main JavaFX Application class. Handles stage setup, FXML loading, and core dependency wiring.
 */
public class JavaFXApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        restartGame();
    }

    private void restartGame() {
        try (Reader statsReader = new InputStreamReader(getClass().getResourceAsStream("/stats.json"))) {
            StatsLoader.init(statsReader);
        } catch (Exception e) {
            System.err.println("Failed to load stats: " + e.getMessage());
        }

        CombatStats heroStats = StatsLoader.getStatsFor("hero");
        AbstractHero hero = new Warrior(heroStats);

        ItemRegistry.init();
        hero.addItem(ItemRegistry.get("HEALTH_POTION"), 3);
        hero.addItem(ItemRegistry.get("FOOD"), 2);

        Map<String, Dungeon> worldMap = new HashMap<>();
        try (Reader dungeonReader = new InputStreamReader(getClass().getResourceAsStream("/dungeons.json"))) {
            Map<String, DungeonDTO> dtos = DungeonLoader.parseDungeons(dungeonReader);
            dtos.forEach((id, dto) -> worldMap.put(id, DungeonFactory.createDungeon(id, dto)));
        } catch (Exception e) {
            System.err.println("Failed to load dungeons: " + e.getMessage());
        }

        GameManager gameManager = new GameManager(hero, worldMap);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
            BorderPane root = loader.load();
            GameController controller = loader.getController();
            
            String saveDir = System.getProperty("user.home") + File.separator + ".dungeoncrawler";
            String saveFile = saveDir + File.separator + "savegame.json";
            SaveManager saveManager = new SaveManager(new FileStorageService(), saveFile);
            
            controller.setSaveManager(saveManager);
            controller.setGameManager(gameManager, this::restartGame);

            Scene scene = new Scene(root, 1050, 750);
            
            primaryStage.setTitle("Dungeon Crawler - Rune & Ink");
            primaryStage.setScene(scene);
            primaryStage.show();

            gameManager.logEvent("Welcome! Choose your next expedition.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Critical error starting application: " + e.getMessage());
        }
    }
}
