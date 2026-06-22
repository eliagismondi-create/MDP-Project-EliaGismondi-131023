package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

public class Armor implements Item {

    @Override
    public String getId() {
        return "ARMOR";
    }

    @Override
    public String getName() {
        return "ARMOR";
    }

    @Override
    public void use(AbstractHero hero) {
        hero.setShield(hero.getShield() + AbstractHero.ARMOR_SHIELD_VALUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Armor armor = (Armor) o;
        return getId().equals(armor.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
