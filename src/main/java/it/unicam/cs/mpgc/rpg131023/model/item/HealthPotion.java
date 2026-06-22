package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

public class HealthPotion implements Item {

    @Override
    public String getId() {
        return "HEALTH_POTION";
    }

    @Override
    public String getName() {
        return "HEALTH POTION";
    }

    @Override
    public void use(AbstractHero hero) {
        if (hero.getHealth() == AbstractHero.MAX_HEALTH) {
            throw new IllegalStateException("Hero is not wounded.");
        }
        hero.restoreHealth(AbstractHero.MAX_HEALTH - hero.getHealth());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HealthPotion that = (HealthPotion) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
