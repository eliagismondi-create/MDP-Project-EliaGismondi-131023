package it.unicam.cs.mpgc.rpg131023.model.item;

/**
 * A consumable item used to restore the hero's hunger levels.
 */
public class Food extends AbstractItem {

    @Override
    public String getId() {
        return "FOOD";
    }

    @Override
    public String getName() {
        return "FOOD";
    }

    /**
     * Fully resets the consumer's hunger to zero. Throws an exception if the consumer is already full.
     */
    @Override
    public void use(ItemConsumer consumer) {
        if (consumer.getHunger() == 0) {
            throw new IllegalStateException("Hero is not hungry.");
        }
        consumer.modifyHunger(-consumer.getHunger());
    }
}
