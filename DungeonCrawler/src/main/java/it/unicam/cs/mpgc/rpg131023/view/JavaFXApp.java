package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.GameManager;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonLoader;
import it.unicam.cs.mpgc.rpg131023.utils.StatsLoader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Map;

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
        CombatStats heroStats = StatsLoader.getStatsFor("hero");
        Hero hero = new Hero(heroStats);

        hero.addResource(ResourceType.HEALTH_POTION, 3);
        hero.addResource(ResourceType.FOOD, 2);

        Map<String, Dungeon> worldMap = DungeonLoader.getAllDungeons();
        GameManager gameManager = new GameManager(hero, worldMap);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
            BorderPane root = loader.load();
            GameController controller = loader.getController();
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
