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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        // Inizializzazione del dominio tramite le utility
        CombatStats heroStats = StatsLoader.getStatsFor("hero");
        Hero hero = new Hero(heroStats);
        
        // Simulo equipaggiamento iniziale per dare un po' di risorse all'eroe
        hero.addResource(ResourceType.HEALTH_POTION, 3);
        hero.addResource(ResourceType.FOOD, 2);

        Map<String, Dungeon> worldMap = DungeonLoader.getAllDungeons();

        this.gameManager = new GameManager(hero, worldMap);

        // Inizializzazione della UI (BorderPane)
        this.rootPane = new BorderPane();
        this.rootPane.setPadding(new Insets(10));
        
        this.eventLog = new TextArea();
        this.eventLog.setEditable(false);
        this.eventLog.setPrefHeight(150);
        this.eventLog.setFont(Font.font("Monospaced", 14));
        this.rootPane.setBottom(this.eventLog);

        Scene scene = new Scene(this.rootPane, 1000, 700);
        primaryStage.setTitle("Dungeon Crawler RPG");
        primaryStage.setScene(scene);
        primaryStage.show();

        showWelcomeMessage();
        refreshCurrentState();
    }

    /**
     * Aggiorna completamente la UI in base allo stato attuale del GameManager.
     */
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

    /**
     * Crea un pannello riutilizzabile per mostrare le statistiche dell'eroe.
     */
    private VBox createHeroStatsPanel(Hero hero) {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f0f0f0;");
        panel.setPrefWidth(250);

        Label title = new Label("Eroe");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label hp = new Label("Salute: " + hero.getHealth() + "/100");
        Label shield = new Label("Scudo: " + hero.getShield());
        Label hunger = new Label("Fame: " + hero.getHunger());
        Label sword = new Label("Spada Equipaggiata: " + (hero.isSwordEquipped() ? "Si" : "No"));
        Label dmg = new Label("Danno Base: " + hero.getDamage());

        Label invTitle = new Label("Inventario:");
        invTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        VBox invList = new VBox(5);
        for (Map.Entry<ResourceType, Integer> entry : hero.getResources().entrySet()) {
            invList.getChildren().add(new Label("- " + entry.getKey() + ": " + entry.getValue()));
        }

        // Pulsanti di azione rapida
        Button healBtn = new Button("Usa Pozione");
        healBtn.setMaxWidth(Double.MAX_VALUE);
        healBtn.setOnAction(e -> {
            try {
                hero.heal();
                showMessage("Hai usato una pozione di cura.");
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage());
            }
        });

        Button eatBtn = new Button("Mangia Cibo");
        eatBtn.setMaxWidth(Double.MAX_VALUE);
        eatBtn.setOnAction(e -> {
            try {
                hero.eat();
                showMessage("Hai mangiato del cibo. Fame azzerata.");
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage());
            }
        });

        Button equipSwordBtn = new Button("Equipaggia Spada");
        equipSwordBtn.setMaxWidth(Double.MAX_VALUE);
        equipSwordBtn.setOnAction(e -> {
            try {
                hero.equipSword();
                showMessage("Hai equipaggiato la spada!");
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage());
            }
        });

        Button equipArmorBtn = new Button("Equipaggia Armatura");
        equipArmorBtn.setMaxWidth(Double.MAX_VALUE);
        equipArmorBtn.setOnAction(e -> {
            try {
                hero.equipArmor();
                showMessage("Hai equipaggiato l'armatura (+50 Scudo)!");
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Errore: " + ex.getMessage());
            }
        });

        panel.getChildren().addAll(
            title, hp, shield, hunger, sword, dmg, 
            invTitle, invList, 
            healBtn, eatBtn, equipSwordBtn, equipArmorBtn
        );
        return panel;
    }

    @Override
    public void showWelcomeMessage() {
        showMessage("Benvenuto nell'HUB! Scegli un dungeon e preparati per l'esplorazione.");
    }

    @Override
    public void displayHub(Hero hero, Map<String, Dungeon> worldMap) {
        this.rootPane.setLeft(createHeroStatsPanel(hero));
        this.rootPane.setRight(null); // Pulisce il lato destro

        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);
        Label title = new Label("Seleziona un Dungeon da esplorare:");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(10));
        tilePane.setHgap(20);
        tilePane.setVgap(20);
        tilePane.setAlignment(Pos.CENTER);

        for (Dungeon dungeon : worldMap.values()) {
            VBox dungeonCard = new VBox(10);
            dungeonCard.setAlignment(Pos.CENTER);
            dungeonCard.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: white; -fx-border-radius: 5;");
            
            // Carica l'immagine generata
            String imagePath = "/assets/" + dungeon.getId() + ".png";
            try {
                Image img = new Image(getClass().getResourceAsStream(imagePath));
                ImageView iv = new ImageView(img);
                iv.setFitWidth(250);
                iv.setPreserveRatio(true);
                dungeonCard.getChildren().add(iv);
            } catch (Exception e) {
                // Immagine non trovata, ignoriamo
            }

            Label dName = new Label(dungeon.getName());
            dName.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            
            Button enterBtn = new Button("Esplora");
            enterBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            enterBtn.setOnAction(e -> {
                try {
                    showMessage("Entri nel dungeon: " + dungeon.getName());
                    this.gameManager.enterDungeon(dungeon.getId());
                    refreshCurrentState();
                } catch (Exception ex) {
                    showMessage("Errore: " + ex.getMessage());
                }
            });
            
            dungeonCard.getChildren().addAll(dName, enterBtn);
            tilePane.getChildren().add(dungeonCard);
        }

        ScrollPane scrollPane = new ScrollPane(tilePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        center.getChildren().addAll(title, scrollPane);
        this.rootPane.setCenter(center);
    }

    @Override
    public void displayCombat(CombatManager combatManager) {
        Hero hero = combatManager.getHero();
        AbstractEnemy enemy = combatManager.getEnemy();

        this.rootPane.setLeft(createHeroStatsPanel(hero));

        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);
        
        Label title = new Label("Combattimento in corso!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: darkred;");

        Button attackBtn = new Button("ATTACCA (Spada Incrociata)");
        attackBtn.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        attackBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-padding: 15 30; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        // Logica di attacco alternato istantaneo
        attackBtn.setOnAction(e -> {
            try {
                // Attacco dell'Eroe
                int heroHpBefore = hero.getHealth();
                int enemyHpBefore = enemy.getHealth();
                
                combatManager.executeNextTurn();
                int damageToEnemy = enemyHpBefore - enemy.getHealth();
                showMessage("Hai attaccato il nemico! Inflitti " + damageToEnemy + " danni.");
                
                // Se il nemico è ancora vivo, contrattacca
                if (!combatManager.isCombatOver()) {
                    combatManager.executeNextTurn();
                    int damageToHero = heroHpBefore - hero.getHealth();
                    showMessage("Il nemico contrattacca! Inflitti " + damageToHero + " danni.");
                }
                
                // Verifica fine combattimento
                if (combatManager.isCombatOver()) {
                    if (combatManager.isHeroVictorious()) {
                        showMessage("VITTORIA! Hai sconfitto il nemico e ottenuto il bottino.");
                    } else {
                        showMessage("SEI MORTO. Hai fallito.");
                    }
                    this.gameManager.resolveCombatEnd();
                }
                
                refreshCurrentState();
            } catch (Exception ex) {
                showMessage("Errore durante il combattimento: " + ex.getMessage());
            }
        });

        center.getChildren().addAll(title, attackBtn);
        this.rootPane.setCenter(center);

        // Pannello destro per il Nemico
        VBox right = new VBox(10);
        right.setPadding(new Insets(10));
        right.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-background-color: #ffe6e6;");
        right.setPrefWidth(250);

        Label eTitle = new Label("Nemico: " + enemy.getClass().getSimpleName());
        eTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        eTitle.setStyle("-fx-text-fill: darkred;");
        
        Label eHp = new Label("Salute: " + enemy.getHealth());
        eHp.setFont(Font.font("Arial", 16));
        Label eDmg = new Label("Danno base: " + enemy.getDamage());

        right.getChildren().addAll(eTitle, eHp, eDmg);
        this.rootPane.setRight(right);
    }

    @Override
    public void displayGameOver() {
        this.rootPane.setLeft(null);
        this.rootPane.setRight(null);

        VBox center = new VBox(30);
        center.setAlignment(Pos.CENTER);
        
        Label title = new Label("GAME OVER");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        title.setStyle("-fx-text-fill: darkred;");
        
        Label sub = new Label("Sei caduto in battaglia...");
        sub.setFont(Font.font("Arial", 24));

        center.getChildren().addAll(title, sub);
        this.rootPane.setCenter(center);
        showMessage("--- GIOCO TERMINATO ---");
    }

    @Override
    public void showMessage(String message) {
        this.eventLog.appendText(message + "\n");
    }
}
