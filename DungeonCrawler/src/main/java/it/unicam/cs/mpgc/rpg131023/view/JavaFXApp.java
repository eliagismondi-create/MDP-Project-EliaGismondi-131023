package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.CombatManager;
import it.unicam.cs.mpgc.rpg131023.controller.GameManager;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonLoader;
import it.unicam.cs.mpgc.rpg131023.utils.StatsLoader;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class JavaFXApp extends Application implements GameView {

    private GameManager gameManager;
    private BorderPane rootPane;
    private TextArea eventLog;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.rootPane = new BorderPane();
        this.rootPane.setPadding(new Insets(20));
        
        this.eventLog = new TextArea();
        this.eventLog.setEditable(false);
        this.eventLog.setPrefHeight(100);
        this.eventLog.getStyleClass().add("ink-log");
        this.eventLog.setWrapText(true);
        this.rootPane.setBottom(this.eventLog);

        Scene scene = new Scene(this.rootPane, 1050, 750);
        try {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Unable to load styles.css");
        }

        primaryStage.setTitle("Dungeon Crawler - Rune & Ink");
        primaryStage.setScene(scene);
        primaryStage.show();

        restartGame();
    }

    private void restartGame() {
        CombatStats heroStats = StatsLoader.getStatsFor("hero");
        Hero hero = new Hero(heroStats);
        
        hero.addResource(ResourceType.HEALTH_POTION, 3);
        hero.addResource(ResourceType.FOOD, 2);

        Map<String, Dungeon> worldMap = DungeonLoader.getAllDungeons();
        this.gameManager = new GameManager(hero, worldMap);

        this.eventLog.clear();
        showWelcomeMessage();
        refreshCurrentState();
    }

    private void refreshCurrentState() {
        switch (this.gameManager.getCurrentState()) {
            case HUB:
                displayHub(this.gameManager.getHero(), DungeonLoader.getAllDungeons());
                break;
            case IN_COMBAT:
                displayCombat(this.gameManager.getActiveCombat());
                break;
            case GAME_OVER:
                displayGameOver();
                break;
        }
    }

    private HBox createStatRow(String key, String value) {
        HBox row = new HBox(5);
        Label keyLabel = new Label(key);
        keyLabel.getStyleClass().add("ink-stat-key");
        Label valLabel = new Label(value);
        valLabel.getStyleClass().add("ink-stat-val");
        row.getChildren().addAll(keyLabel, valLabel);
        return row;
    }

    private VBox createHeroStatsPanel(Hero hero) {
        VBox panel = new VBox(15);
        panel.getStyleClass().add("ink-panel");
        panel.setPrefWidth(220);

        Label title = new Label("HERO STATS");
        title.getStyleClass().add("ink-title");

        Region separator1 = new Region();
        separator1.getStyleClass().add("ink-separator");
        separator1.setMinHeight(2);

        VBox statsBox = new VBox(8);
        statsBox.getChildren().addAll(
            createStatRow("Health:", hero.getHealth() + "/100"),
            createStatRow("Shield:", String.valueOf(hero.getShield())),
            createStatRow("Hunger:", String.valueOf(hero.getHunger())),
            createStatRow("Sword:", hero.isSwordEquipped() ? "YES" : "NO"),
            createStatRow("Base Damage:", String.valueOf(hero.getDamage()))
        );

        Region separator2 = new Region();
        separator2.getStyleClass().add("ink-separator");
        separator2.setMinHeight(2);

        VBox invBox = new VBox(5);
        Label invTitle = new Label("INVENTORY");
        invTitle.getStyleClass().add("ink-stat-key");
        invBox.getChildren().add(invTitle);
        
        for (Map.Entry<ResourceType, Integer> entry : hero.getResources().entrySet()) {
            Label item = new Label("⬡ " + entry.getKey() + ": " + entry.getValue());
            item.getStyleClass().add("ink-stat-val");
            invBox.getChildren().add(item);
        }

        Region separator3 = new Region();
        separator3.getStyleClass().add("ink-separator");
        separator3.setMinHeight(2);

        GridPane actionGrid = new GridPane();
        actionGrid.setHgap(10);
        actionGrid.setVgap(10);
        actionGrid.setAlignment(Pos.CENTER);

        Button healBtn = createInkButton("POTION");
        healBtn.setOnAction(e -> {
            try { hero.heal(); showMessage("Drank a healing potion."); refreshCurrentState(); }
            catch (Exception ex) { showMessage("Error: " + ex.getMessage()); }
        });

        Button eatBtn = createInkButton("FOOD");
        eatBtn.setOnAction(e -> {
            try { hero.eat(); showMessage("Ate a food ration."); refreshCurrentState(); }
            catch (Exception ex) { showMessage("Error: " + ex.getMessage()); }
        });

        Button swordBtn = createInkButton("SWORD");
        swordBtn.setOnAction(e -> {
            try { hero.equipSword(); showMessage("Sword equipped."); refreshCurrentState(); }
            catch (Exception ex) { showMessage("Error: " + ex.getMessage()); }
        });

        Button armorBtn = createInkButton("SHIELD");
        armorBtn.setOnAction(e -> {
            try { hero.equipArmor(); showMessage("Armor equipped."); refreshCurrentState(); }
            catch (Exception ex) { showMessage("Error: " + ex.getMessage()); }
        });

        actionGrid.add(healBtn, 0, 0);
        actionGrid.add(eatBtn, 1, 0);
        actionGrid.add(swordBtn, 0, 1);
        actionGrid.add(armorBtn, 1, 1);

        panel.getChildren().addAll(title, separator1, statsBox, separator2, invBox, separator3, actionGrid);
        return panel;
    }

    private Button createInkButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("ink-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    @Override
    public void showWelcomeMessage() {
        showMessage("Welcome to the HUB! Choose your next expedition.");
    }

    @Override
    public void displayHub(Hero hero, Map<String, Dungeon> worldMap) {
        this.rootPane.setLeft(createHeroStatsPanel(hero));
        this.rootPane.setRight(null);

        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        
        Label title = new Label("SELECT A DUNGEON TO EXPLORE");
        title.getStyleClass().add("ink-title");
        title.setStyle("-fx-font-size: 24px;");
        
        HBox dungeonRow = new HBox(20);
        dungeonRow.setAlignment(Pos.CENTER);

        for (Dungeon dungeon : worldMap.values()) {
            VBox card = new VBox(15);
            card.setAlignment(Pos.CENTER);
            card.getStyleClass().add("ink-dungeon-card");
            card.setPrefWidth(240);
            
            try {
                String imgName = dungeon.getId() + ".png";
                Image img = new Image(getClass().getResourceAsStream("/assets/" + imgName));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(220);
                iv.setPreserveRatio(true);
                
                VBox imageBox = new VBox(iv);
                imageBox.setStyle("-fx-background-color: #f0ebe0; -fx-border-color: transparent transparent #2c2418 transparent; -fx-border-width: 0 0 1.5px 0;");
                card.getChildren().add(imageBox);
            } catch (Exception e) {
                // Ignore missing images
            }

            Label dName = new Label(dungeon.getName().toUpperCase());
            dName.getStyleClass().add("ink-title");
            dName.setStyle("-fx-font-size: 16px; -fx-padding: 0;");
            
            // Hardcoded generic diff test
            String diff = dungeon.getId().contains("bandit") ? "DIFFICULTY: NORMAL" : "DIFFICULTY: HARD";
            Label dSub = new Label(diff);
            dSub.getStyleClass().add("ink-stat-key");

            Button enterBtn = createInkButton("EXPLORE");
            enterBtn.setOnAction(e -> {
                try {
                    showMessage("You venture into the dungeon: " + dungeon.getName());
                    this.gameManager.enterDungeon(dungeon.getId());
                    refreshCurrentState();
                } catch (Exception ex) {
                    showMessage("Error: " + ex.getMessage());
                }
            });
            
            card.getChildren().addAll(dName, dSub, enterBtn);
            dungeonRow.getChildren().add(card);
        }

        center.getChildren().addAll(title, dungeonRow);
        this.rootPane.setCenter(center);
    }

    @Override
    public void displayCombat(CombatManager combatManager) {
        Hero hero = combatManager.getHero();
        AbstractEnemy enemy = combatManager.getEnemy();

        this.rootPane.setLeft(createHeroStatsPanel(hero));

        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        
        Label title = new Label("COMBAT");
        title.getStyleClass().add("ink-title");
        title.setStyle("-fx-font-size: 32px;");

        Button attackBtn = createInkButton("ATTACK ENEMY");
        attackBtn.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
        
        attackBtn.setOnAction(e -> {
            try {
                int enemyHpBefore = enemy.getHealth();
                combatManager.executeNextTurn();
                int dmg = enemyHpBefore - enemy.getHealth();
                showMessage("You land a hit! Dealt " + dmg + " damage.");
                
                if (!combatManager.isCombatOver()) {
                    int heroHpBefore = hero.getHealth();
                    combatManager.executeNextTurn();
                    int dmgHero = heroHpBefore - hero.getHealth();
                    showMessage("The enemy counterattacks. Received " + dmgHero + " damage.");
                }
                
                if (combatManager.isCombatOver()) {
                    if (combatManager.isHeroVictorious()) {
                        showMessage("VICTORY! Enemy defeated.");
                    } else {
                        showMessage("DEFEAT! You have fallen in battle.");
                    }
                    this.gameManager.resolveCombatEnd();
                }
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Combat error: " + ex.getMessage());
            }
        });

        center.getChildren().addAll(title, attackBtn);
        this.rootPane.setCenter(center);

        VBox right = new VBox(15);
        right.getStyleClass().add("ink-panel");
        right.setPrefWidth(200);

        Label eTitle = new Label("ENEMY");
        eTitle.getStyleClass().add("ink-title");

        Region sep = new Region();
        sep.getStyleClass().add("ink-separator");
        sep.setMinHeight(2);

        VBox statsBox = new VBox(10);
        statsBox.getChildren().addAll(
            createStatRow("Type:", enemy.getClass().getSimpleName()),
            createStatRow("Health:", String.valueOf(enemy.getHealth())),
            createStatRow("Damage:", String.valueOf(enemy.getDamage()))
        );

        right.getChildren().addAll(eTitle, sep, statsBox);
        this.rootPane.setRight(right);
    }

    @Override
    public void displayGameOver() {
        this.rootPane.setLeft(null);
        this.rootPane.setRight(null);

        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        
        Label title = new Label("YOUR JOURNEY ENDS HERE");
        title.getStyleClass().add("ink-title");
        title.setStyle("-fx-font-size: 42px;");
        
        Label sub = new Label("The hero has fallen. Game Over.");
        sub.getStyleClass().add("ink-stat-key");
        sub.setStyle("-fx-font-size: 24px;");

        Button restartBtn = createInkButton("RESTART");
        restartBtn.setMaxWidth(200);
        restartBtn.setOnAction(e -> restartGame());

        center.getChildren().addAll(title, sub, restartBtn);
        this.rootPane.setCenter(center);
        showMessage("The adventure comes to an end.");
    }

    @Override
    public void showMessage(String message) {
        this.eventLog.appendText("· " + message + "\n");
    }
}
