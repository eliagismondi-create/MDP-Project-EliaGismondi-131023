package it.unicam.cs.mpgc.rpg131023.model.player;

import java.beans.PropertyChangeSupport;

/**
 * Handles experience points and level progression logic.
 */
public class LevelSystem {
    public static final int XP_PER_LEVEL = 100;

    private int xp = 0;
    private int level = 1;
    private final PropertyChangeSupport support;

    public LevelSystem(PropertyChangeSupport support) {
        this.support = support;
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        int old = this.xp;
        this.xp = xp;
        support.firePropertyChange("xp", old, this.xp);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        int old = this.level;
        this.level = level;
        support.firePropertyChange("level", old, this.level);
    }

    public void addXp(int amount) {
        if (amount <= 0) {
            return;
        }
        int totalXp = this.xp + amount;
        int levelsGained = totalXp / XP_PER_LEVEL;
        int remainingXp = totalXp % XP_PER_LEVEL;

        if (levelsGained > 0) {
            setLevel(this.level + levelsGained);
        }
        setXp(remainingXp);
    }
}
