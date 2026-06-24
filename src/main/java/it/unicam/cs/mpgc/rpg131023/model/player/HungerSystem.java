package it.unicam.cs.mpgc.rpg131023.model.player;

import java.beans.PropertyChangeSupport;

/**
 * Tracks hunger and exploration fatigue, triggering starvation when maximum hunger is reached.
 */
public class HungerSystem {
    public static final int MAX_HUNGER = 100;
    public static final int EXPLORATION_FATIGUE = 25;

    private int hunger = 0;
    private final PropertyChangeSupport support;
    private final Runnable onStarvation;

    public HungerSystem(PropertyChangeSupport support, Runnable onStarvation) {
        this.support = support;
        this.onStarvation = onStarvation;
    }

    public int getHunger() {
        return this.hunger;
    }

    public void setHunger(int hunger) {
        if (hunger < 0 || hunger > MAX_HUNGER) {
            throw new IllegalArgumentException("Hunger must be between 0 and " + MAX_HUNGER);
        }
        int old = this.hunger;
        this.hunger = hunger;
        support.firePropertyChange("hunger", old, this.hunger);
    }

    public void modifyHunger(int amount) {
        setHunger(Math.max(0, Math.min(MAX_HUNGER, this.hunger + amount)));
    }

    public void addHunger(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Hunger increase cannot be negative.");
        }
        int old = this.hunger;
        this.hunger += amount;
        if (this.hunger >= MAX_HUNGER) {
            this.hunger = MAX_HUNGER;
            support.firePropertyChange("hunger", old, this.hunger);
            if (this.onStarvation != null) {
                this.onStarvation.run();
            }
        } else {
            support.firePropertyChange("hunger", old, this.hunger);
        }
    }

    public void sufferFatigue() {
        this.addHunger(EXPLORATION_FATIGUE);
    }
}
