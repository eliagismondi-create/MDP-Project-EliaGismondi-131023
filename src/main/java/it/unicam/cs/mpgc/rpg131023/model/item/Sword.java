package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

/**
 * A weapon item that grants the hero an offensive buff when used.
 */
public class Sword implements Item {

    @Override
    public String getId() {
        return "SWORD";
    }

    @Override
    public String getName() {
        return "SWORD";
    }

    /**
     * Applies maximum durability buff to the hero's attack power.
     */
    @Override
    public void use(AbstractHero hero) {
        hero.getEquipment().addBuff("SWORD", AbstractHero.MAX_SWORD_DURABILITY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sword sword = (Sword) o;
        return getId().equals(sword.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
