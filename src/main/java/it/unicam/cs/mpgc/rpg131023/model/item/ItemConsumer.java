package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.EquipmentManager;

/**
 * Abstraction for any entity capable of consuming items.
 * Decouples {@link Item#use(ItemConsumer)} from concrete hero implementations,
 * enabling future consumers (e.g. NPCs) without forcing inheritance from AbstractHero.
 */
public interface ItemConsumer {

    /**
     * Restores health points up to the entity's cap.
     *
     * @param amount The amount of health to restore.
     */
    void restoreHealth(int amount);

    /**
     * Adjusts the hunger level by the given delta (positive or negative).
     *
     * @param amount The hunger modification amount.
     */
    void modifyHunger(int amount);

    /**
     * @return The entity's current health.
     */
    int getHealth();

    /**
     * @return The entity's maximum health.
     */
    int getMaxHealth();

    /**
     * @return The entity's current hunger level.
     */
    int getHunger();

    /**
     * @return The equipment manager controlling active buffs.
     */
    EquipmentManager getEquipment();
}
