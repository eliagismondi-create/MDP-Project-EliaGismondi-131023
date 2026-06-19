package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.CombatManager;
import it.unicam.cs.mpgc.rpg131023.controller.GameManager;
import it.unicam.cs.mpgc.rpg131023.controller.GameManager.GameState;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.Dungeon;
import it.unicam.cs.mpgc.rpg131023.model.enemy.AbstractEnemy;
import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.persistence.HeroSaveDTO;
import it.unicam.cs.mpgc.rpg131023.persistence.SaveManager;
import it.unicam.cs.mpgc.rpg131023.utils.DungeonLoader;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

public class GameController {

    @FXML private VBox heroStatsPanel;
    @FXML private Label lblHeroHealth;
    @FXML private Label lblHeroArmor;
    @FXML private Label lblHeroHunger;
    @FXML private Label lblHeroSword;
    @FXML private ProgressBar barSwordDurability;
    @FXML private HBox boxSword;
    @FXML private VBox invBox;
    
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
    private Runnable onRestart;

    public void setGameManager(GameManager gameManager, Runnable onRestart) {
        this.gameManager = gameManager;
        this.onRestart = onRestart;
        
        setupBindings();
        populateDungeons();
        updateFullUI();
    }

    private void setupBindings() {
        Hero hero = gameManager.getHero();
        
        hero.addPropertyChangeListener(evt -> Platform.runLater(this::updateHeroUI));
        
        gameManager.addPropertyChangeListener(evt -> Platform.runLater(() -> {
            String propName = evt.getPropertyName();
            if ("currentState".equals(propName)) {
                updateViewsVisibility();
            } else if ("activeCombat".equals(propName)) {
                CombatManager cm = (CombatManager) evt.getNewValue();
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
        updateHeroUI();
        updateViewsVisibility();
        CombatManager cm = gameManager.getActiveCombat();
        if (cm != null) {
            bindEnemy(cm.getEnemy());
            btnCombatBack.setVisible(!cm.hasCombatStarted());
            btnCombatBack.setManaged(!cm.hasCombatStarted());
        }
    }

    private void updateViewsVisibility() {
        GameState state = gameManager.getCurrentState();
        hubView.setVisible(state == GameState.HUB);
        hubView.setManaged(state == GameState.HUB);
        
        combatView.setVisible(state == GameState.IN_COMBAT);
        combatView.setManaged(state == GameState.IN_COMBAT);
        
        gameOverView.setVisible(state == GameState.GAME_OVER);
        gameOverView.setManaged(state == GameState.GAME_OVER);
        
        heroStatsPanel.setVisible(state != GameState.GAME_OVER);
        enemyStatsPanel.setVisible(state == GameState.IN_COMBAT);
    }

    private void updateHeroUI() {
        Hero hero = gameManager.getHero();
        lblHeroHealth.setText(hero.getHealth() + "/" + Hero.MAX_HEALTH);
        lblHeroArmor.setText(String.valueOf(hero.getShield()));
        lblHeroHunger.setText(String.valueOf(hero.getHunger()));
        lblHeroSword.setText(hero.isSwordEquipped() ? "YES" : "NO");
        barSwordDurability.setProgress((double) hero.getSwordDurability() / Hero.MAX_SWORD_DURABILITY);
        barSwordDurability.setVisible(hero.isSwordEquipped());
        updateInventoryUI();
    }

    private void updateInventoryUI() {
        if (invBox.getChildren().size() > 1) {
            invBox.getChildren().remove(1, invBox.getChildren().size());
        }
        gameManager.getHero().getResources().forEach((key, value) -> {
            String resourceName = key.toString().replace("_", " ");
            Label item = new Label("⬡ " + resourceName + ": " + value);
            item.getStyleClass().add("ink-stat-val");
            invBox.getChildren().add(item);
        });
    }

    private void populateDungeons() {
        Map<String, Dungeon> worldMap = DungeonLoader.getAllDungeons();
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

    private void bindEnemy(AbstractEnemy enemy) {
        lblEnemyType.setText(enemy.getClass().getSimpleName());
        lblEnemyHealth.setText(String.valueOf(enemy.getHealth()));
        lblEnemyDamage.setText(String.valueOf(enemy.getDamage()));
        
        enemy.addPropertyChangeListener(evt -> {
            if ("health".equals(evt.getPropertyName())) {
                Platform.runLater(() -> lblEnemyHealth.setText(String.valueOf(evt.getNewValue())));
            }
        });
    }

    private void executeHeroAction(Runnable action, String successMessage) {
        try {
            action.run();
            gameManager.logEvent(successMessage);
        } catch (Exception ex) {
            gameManager.logEvent("Error: " + ex.getMessage());
        }
    }

    @FXML
    private void handlePotion(ActionEvent event) {
        executeHeroAction(gameManager.getHero()::heal, "Drank a healing potion.");
    }

    @FXML
    private void handleFood(ActionEvent event) {
        executeHeroAction(gameManager.getHero()::eat, "Ate a food ration.");
    }

    @FXML
    private void handleSword(ActionEvent event) {
        executeHeroAction(gameManager.getHero()::equipSword, "Sword equipped.");
    }

    @FXML
    private void handleArmor(ActionEvent event) {
        executeHeroAction(gameManager.getHero()::equipArmor, "Armor equipped.");
    }

    @FXML
    private void handleRetreat(ActionEvent event) {
        gameManager.retreatFromDungeon();
        gameManager.logEvent("You fled the dungeon, but hunger still strikes.");
    }

    @FXML
    private void handleAttack(ActionEvent event) {
        try {
            CombatManager cm = gameManager.getActiveCombat();
            AbstractEnemy enemy = cm.getEnemy();
            Hero hero = cm.getHero();
            
            int enemyHpBefore = enemy.getHealth();
            cm.executeNextTurn();
            int dmg = enemyHpBefore - enemy.getHealth();
            gameManager.logEvent("You land a hit! Dealt " + dmg + " damage.");

            if (!cm.isCombatOver()) {
                int heroHpBefore = hero.getHealth();
                cm.executeNextTurn();
                int dmgHero = heroHpBefore - hero.getHealth();
                gameManager.logEvent("The enemy counterattacks. Received " + dmgHero + " damage.");
            }

            if (cm.isCombatOver()) {
                if (cm.isHeroVictorious()) {
                    gameManager.logEvent("VICTORY! Enemy defeated.");
                } else {
                    gameManager.logEvent("DEFEAT! You have fallen in battle.");
                }
                gameManager.resolveCombatEnd();
            }
            
            btnCombatBack.setVisible(!cm.hasCombatStarted());
            btnCombatBack.setManaged(!cm.hasCombatStarted());
            
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
            SaveManager.saveGame(gameManager.getHero());
            gameManager.logEvent("Game saved successfully.");
        } catch (Exception ex) {
            gameManager.logEvent("Error saving game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleLoad(ActionEvent event) {
        try {
            HeroSaveDTO dto = SaveManager.loadGame();
            dto.applyToHero(gameManager.getHero());
            gameManager.logEvent("Game loaded successfully.");
        } catch (Exception ex) {
            gameManager.logEvent("Error loading game: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
