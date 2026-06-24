package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.core.CombatManager;
import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;
import it.unicam.cs.mpgc.rpg131023.controller.state.StateType;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.Enemy;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.persistence.HeroSaveDTO;
import it.unicam.cs.mpgc.rpg131023.persistence.SaveManager;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Binds the JavaFX UI elements to the GameManager data and handles user interactions.
 */
public class GameController {

    @FXML private HeroStatsController heroStatsController;
    @FXML private VBox heroStats;

    @FXML private VBox hubView;
    @FXML private HBox dungeonRow;
    
    @FXML private VBox combatView;
    @FXML private Label lblCombatTitle;
    @FXML private VBox lootBox;
    @FXML private Button btnCombatBack;
    @FXML private Button btnCombatAttack;
    
    @FXML private VBox gameOverView;
    
    @FXML private VBox enemyStatsPanel;
    @FXML private Label lblEnemyType;
    @FXML private Label lblEnemyHealth;
    @FXML private Label lblEnemyDamage;
    
    @FXML private TextArea txtEventLog;
    
    private GameManager gameManager;
    private SaveManager saveManager;
    private ItemRegistry itemRegistry;
    private Runnable onRestart;

    public void setSaveManager(SaveManager saveManager) {
        this.saveManager = saveManager;
    }

    /**
     * Wires the game manager, item registry, and restart callback.
     *
     * @param gameManager  The core game controller.
     * @param itemRegistry The item registry for resolving items.
     * @param onRestart    Callback to restart the game.
     */
    public void setGameManager(GameManager gameManager, ItemRegistry itemRegistry, Runnable onRestart) {
        this.gameManager = gameManager;
        this.itemRegistry = itemRegistry;
        this.onRestart = onRestart;
        
        setupBindings();
        populateDungeons();
        updateFullUI();
    }

    private void setupBindings() {
        AbstractHero hero = gameManager.getHero();
        heroStatsController.bindHero(hero, gameManager, itemRegistry);
        
        gameManager.getEventDispatcher().addPropertyChangeListener(evt -> Platform.runLater(() -> {
            String propName = evt.getPropertyName();
            if ("currentState".equals(propName)) {
                updateViewsVisibility();
            } else if ("activeCombat".equals(propName)) {
                @SuppressWarnings("unchecked")
                CombatManager<AbstractHero, Enemy> cm = (CombatManager<AbstractHero, Enemy>) evt.getNewValue();
                if (cm != null) {
                    bindEnemy(cm.getEnemy());
                    btnCombatBack.setVisible(!cm.hasCombatStarted());
                    btnCombatBack.setManaged(!cm.hasCombatStarted());
                } else {
                    btnCombatBack.setVisible(false);
                    btnCombatBack.setManaged(false);
                }
            } else if ("currentDungeon".equals(propName)) {
                Dungeon d = (Dungeon) evt.getNewValue();
                if (d != null) {
                    lblCombatTitle.setText(d.getName().toUpperCase());
                    renderLoot(d);
                }
            } else if ("eventLogAdded".equals(propName)) {
                txtEventLog.appendText((String) evt.getNewValue() + "\n");
            }
        }));
    }

    private void updateFullUI() {
        updateViewsVisibility();
        CombatManager<AbstractHero, Enemy> cm = gameManager.getActiveCombat();
        if (cm != null) {
            bindEnemy(cm.getEnemy());
            btnCombatBack.setVisible(!cm.hasCombatStarted());
            btnCombatBack.setManaged(!cm.hasCombatStarted());
        }
    }

    private void updateViewsVisibility() {
        StateType stateType = gameManager.getCurrentState().getType();
        hubView.setVisible(stateType == StateType.HUB);
        hubView.setManaged(stateType == StateType.HUB);
        
        combatView.setVisible(stateType == StateType.IN_COMBAT);
        combatView.setManaged(stateType == StateType.IN_COMBAT);
        
        gameOverView.setVisible(stateType == StateType.GAME_OVER);
        gameOverView.setManaged(stateType == StateType.GAME_OVER);
        
        if (heroStats != null) {
            heroStats.setVisible(stateType != StateType.GAME_OVER);
        }
        enemyStatsPanel.setVisible(stateType == StateType.IN_COMBAT);
    }

    private void populateDungeons() {
        Map<String, Dungeon> worldMap = gameManager.getWorldMap();
        worldMap.values().forEach(dungeon -> {
            VBox card = new VBox(15);
            card.setAlignment(Pos.CENTER);
            card.getStyleClass().add("ink-dungeon-card");
            card.setPrefWidth(240);

            try {
                Image img = new Image(getClass().getResourceAsStream("/assets/" + dungeon.getId() + ".png"));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(220);
                iv.setPreserveRatio(true);
                VBox imageBox = new VBox(iv);
                imageBox.setStyle("-fx-background-color: #f0ebe0; -fx-border-color: transparent transparent #2c2418 transparent; -fx-border-width: 0 0 1.5px 0;");
                card.getChildren().add(imageBox);
            } catch (Exception e) {
            }

            Label dName = new Label(dungeon.getName().toUpperCase());
            dName.getStyleClass().add("ink-title");
            dName.setStyle("-fx-font-size: 16px; -fx-padding: 0;");

            String diff = "DIFFICULTY: " + dungeon.getDifficulty().name();
            Label dSub = new Label(diff);
            dSub.getStyleClass().add("ink-stat-key");

            Button enterBtn = new Button("EXPLORE");
            enterBtn.getStyleClass().add("ink-button");
            enterBtn.setOnAction(e -> {
                try {
                    gameManager.logEvent("You venture into the dungeon: " + dungeon.getName());
                    gameManager.enterDungeon(dungeon.getId());
                } catch (Exception ex) {
                    gameManager.logEvent("Error: " + ex.getMessage());
                }
            });

            card.getChildren().addAll(dName, dSub, enterBtn);
            dungeonRow.getChildren().add(card);
        });
    }

    private void renderLoot(Dungeon dungeon) {
        if (lootBox.getChildren().size() > 1) {
            lootBox.getChildren().remove(1, lootBox.getChildren().size());
        }
        UILootRendererVisitor visitor = new UILootRendererVisitor();
        dungeon.getTreasures().forEach(loot -> loot.accept(visitor));
        lootBox.getChildren().add(visitor.getGraphic());
    }

    private void bindEnemy(Enemy enemy) {
        lblEnemyType.setText(enemy.getType());
        lblEnemyHealth.setText(String.valueOf(enemy.getHealth()));
        lblEnemyDamage.setText(String.valueOf(enemy.getDamage()));
        
        enemy.addPropertyChangeListener(evt -> {
            if ("health".equals(evt.getPropertyName())) {
                Platform.runLater(() -> lblEnemyHealth.setText(String.valueOf(evt.getNewValue())));
            }
        });
    }

    @FXML
    private void handleRetreat(ActionEvent event) {
        gameManager.retreatFromDungeon();
        gameManager.logEvent("You fled the dungeon, but hunger still strikes.");
    }

    /**
     * Delegates the entire combat round to the GameManager.
     * The View only updates button visibility — no business logic here.
     */
    @FXML
    private void handleAttack(ActionEvent event) {
        try {
            gameManager.executeCombatRound();

            CombatManager<AbstractHero, Enemy> cm = gameManager.getActiveCombat();
            if (cm != null) {
                btnCombatBack.setVisible(!cm.hasCombatStarted());
                btnCombatBack.setManaged(!cm.hasCombatStarted());
            }
        } catch (Exception ex) {
            gameManager.logEvent("Combat error: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRestart(ActionEvent event) {
        if (onRestart != null) {
            onRestart.run();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            if (saveManager != null) {
                saveManager.saveGame(gameManager.getHero());
                gameManager.logEvent("Game saved successfully.");
            } else {
                gameManager.logEvent("Error: SaveManager not initialized.");
            }
        } catch (Exception ex) {
            gameManager.logEvent("Error saving game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        try {
            if (saveManager != null) {
                HeroSaveDTO dto = saveManager.loadGame();
                saveManager.getHeroMapper().updateHeroFromDTO(gameManager.getHero(), dto);
                gameManager.logEvent("Game loaded successfully.");
            } else {
                gameManager.logEvent("Error: SaveManager not initialized.");
            }
        } catch (Exception ex) {
            gameManager.logEvent("Error loading game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
