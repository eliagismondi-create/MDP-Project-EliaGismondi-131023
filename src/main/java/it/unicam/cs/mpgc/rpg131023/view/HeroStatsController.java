package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.controller.core.GameManager;
import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.model.item.Sword;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.model.player.LevelSystem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;

/**
 * Controller specifically for the Hero Stats side panel.
 */
public class HeroStatsController {

    @FXML private VBox heroStatsPanel;
    @FXML private Label lblHeroLevel;
    @FXML private Label lblHeroXp;
    @FXML private Label lblHeroHealth;
    @FXML private Label lblHeroArmor;
    @FXML private Label lblHeroHunger;
    @FXML private Label lblHeroSword;
    @FXML private Label lblHeroDamage;
    @FXML private ProgressBar barSwordDurability;
    @FXML private HBox boxSword;
    @FXML private VBox invBox;
    
    @FXML private Button btnPotion;
    @FXML private Button btnFood;
    @FXML private Button btnSword;
    @FXML private Button btnArmor;

    private GameManager gameManager;
    private AbstractHero hero;
    private ItemRegistry itemRegistry;

    /**
     * Binds the hero and game manager to this controller.
     *
     * @param hero         The player character.
     * @param gameManager  The game controller.
     * @param itemRegistry The item registry for resolving items.
     */
    public void bindHero(AbstractHero hero, GameManager gameManager, ItemRegistry itemRegistry) {
        this.hero = hero;
        this.gameManager = gameManager;
        this.itemRegistry = itemRegistry;
        
        this.hero.addPropertyChangeListener(evt -> Platform.runLater(this::updateHeroUI));
        updateHeroUI();
    }

    private void updateHeroUI() {
        if (this.hero == null) return;
        lblHeroLevel.setText(String.valueOf(hero.getLevel()));
        lblHeroXp.setText(hero.getXp() + "/" + LevelSystem.XP_PER_LEVEL);
        lblHeroHealth.setText(hero.getHealth() + "/" + AbstractHero.MAX_HEALTH);
        lblHeroArmor.setText(String.valueOf(hero.getShield()));
        lblHeroHunger.setText(hero.getHunger() + "/100");
        lblHeroDamage.setText(String.valueOf(hero.getDamage()));

        boolean swordEquipped = hero.isSwordEquipped();
        lblHeroSword.setText(swordEquipped ? "YES" : "NO");
        boxSword.setVisible(swordEquipped);
        if (swordEquipped) {
            barSwordDurability.setProgress((double) hero.getSwordDurability() / Sword.DEFAULT_DURABILITY);
        }

        updateInventoryUI();
    }

    private void updateInventoryUI() {
        if (invBox.getChildren().size() > 1) {
            invBox.getChildren().remove(1, invBox.getChildren().size());
        }

        Map<Item, Integer> inv = hero.getInventory();
        if (inv.isEmpty()) {
            Label emptyLbl = new Label("Empty");
            emptyLbl.getStyleClass().add("ink-stat-val");
            invBox.getChildren().add(emptyLbl);
        } else {
            inv.forEach((item, amount) -> {
                Label lbl = new Label("- " + item.getName() + " x" + amount);
                lbl.getStyleClass().add("ink-stat-val");
                invBox.getChildren().add(lbl);
            });
        }

        btnPotion.setDisable(inv.getOrDefault(itemRegistry.get("HEALTH_POTION"), 0) == 0);
        btnFood.setDisable(inv.getOrDefault(itemRegistry.get("FOOD"), 0) == 0);
        btnSword.setDisable(inv.getOrDefault(itemRegistry.get("SWORD"), 0) == 0);
        btnArmor.setDisable(inv.getOrDefault(itemRegistry.get("ARMOR"), 0) == 0);
    }

    @FXML
    private void handlePotion(ActionEvent event) {
        try {
            hero.useItem(itemRegistry.get("HEALTH_POTION"));
            gameManager.logEvent("Hero drank a Health Potion.");
        } catch (Exception e) {
            gameManager.logEvent(e.getMessage());
        }
    }

    @FXML
    private void handleFood(ActionEvent event) {
        try {
            hero.useItem(itemRegistry.get("FOOD"));
            gameManager.logEvent("Hero ate some Food.");
        } catch (Exception e) {
            gameManager.logEvent(e.getMessage());
        }
    }

    @FXML
    private void handleSword(ActionEvent event) {
        try {
            hero.useItem(itemRegistry.get("SWORD"));
            gameManager.logEvent("Hero equipped a Sword.");
        } catch (Exception e) {
            gameManager.logEvent(e.getMessage());
        }
    }

    @FXML
    private void handleArmor(ActionEvent event) {
        try {
            hero.useItem(itemRegistry.get("ARMOR"));
            gameManager.logEvent("Hero equipped Armor.");
        } catch (Exception e) {
            gameManager.logEvent(e.getMessage());
        }
    }
}
