package it.unicam.cs.mpgc.rpg131023.view;

import it.unicam.cs.mpgc.rpg131023.model.dungeon.LootVisitor;
import it.unicam.cs.mpgc.rpg131023.model.dungeon.ResourceLoot;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Generates JavaFX UI components for the different types of loot.
 */
public class UILootRendererVisitor implements LootVisitor {
    private final VBox container;

    public UILootRendererVisitor() {
        this.container = new VBox(5);
    }

    @Override
    public void visit(ResourceLoot loot) {
        String name = loot.getType().toString().replace("_", " ");
        Label lbl = new Label("⬡ " + name + ": " + loot.getAmount());
        lbl.getStyleClass().add("ink-stat-val");
        this.container.getChildren().add(lbl);
    }

    /**
     * @return Il contenitore con tutti i nodi generati.
     */
    public Node getGraphic() {
        return this.container;
    }
}
