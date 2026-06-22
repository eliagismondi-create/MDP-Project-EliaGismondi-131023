package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

public class Sword implements Item {

    @Override
    public String getId() {
        return "SWORD";
    }

    @Override
    public String getName() {
        return "SWORD";
    }

    @Override
    public void use(AbstractHero hero) {
        hero.setSwordEquipped(true);
        hero.setSwordDurability(AbstractHero.MAX_SWORD_DURABILITY);
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
