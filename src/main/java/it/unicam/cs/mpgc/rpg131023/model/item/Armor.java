package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.BuffType;

/**
 * A defensive item that provides a shield buff to mitigate incoming damage.
 */
public class Armor extends AbstractItem {

    /** Amount of damage the armor can absorb before breaking. */
    public static final int SHIELD_VALUE = 50;

    @Override
    public String getId() {
        return "ARMOR";
    }

    @Override
    public String getName() {
        return "ARMOR";
    }

    /**
     * Equips the shield buff on the consumer with a fixed durability value.
     */
    @Override
    public void use(ItemConsumer consumer) {
        consumer.getEquipment().addBuff(BuffType.SHIELD, SHIELD_VALUE);
    }
}
