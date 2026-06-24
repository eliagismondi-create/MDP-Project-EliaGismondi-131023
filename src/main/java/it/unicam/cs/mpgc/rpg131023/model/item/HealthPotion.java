package it.unicam.cs.mpgc.rpg131023.model.item;

/**
 * A consumable potion that restores missing health points.
 */
public class HealthPotion extends AbstractItem {

    @Override
    public String getId() {
        return "HEALTH_POTION";
    }

    @Override
    public String getName() {
        return "HEALTH POTION";
    }

    /**
     * Fully heals the consumer. Throws an exception if the consumer is already at maximum health.
     */
    @Override
    public void use(ItemConsumer consumer) {
        if (consumer.getHealth() == consumer.getMaxHealth()) {
            throw new IllegalStateException("Hero is not wounded.");
        }
        consumer.restoreHealth(consumer.getMaxHealth() - consumer.getHealth());
    }
}
