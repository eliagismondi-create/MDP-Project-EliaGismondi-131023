package it.unicam.cs.mpgc.rpg131023.model.item;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

/**
 * A consumable item used to restore the hero's hunger levels.
 */
public class Food implements Item {

    @Override
    public String getId() {
        return "FOOD";
    }

    @Override
    public String getName() {
        return "FOOD";
    }

    /**
     * Fully resets the hero's hunger to zero. Throws an exception if the hero is already full.
     */
    @Override
    public void use(AbstractHero hero) {
        if (hero.getHunger() == 0) {
            throw new IllegalStateException("Hero is not hungry.");
        }
        hero.modifyHunger(-hero.getHunger()); // Set to 0
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return getId().equals(food.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
