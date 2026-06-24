package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.BuffType;

/**
 * A weapon item that grants the hero an offensive buff when used.
 */
public class Sword extends AbstractItem {

    /** Number of attacks the sword lasts before breaking. */
    public static final int DEFAULT_DURABILITY = 3;

    @Override
    public String getId() {
        return "SWORD";
    }

    @Override
    public String getName() {
        return "SWORD";
    }

    /**
     * Applies maximum durability buff to the consumer's attack power.
     */
    @Override
    public void use(ItemConsumer consumer) {
        consumer.getEquipment().addBuff(BuffType.SWORD, DEFAULT_DURABILITY);
    }
}
