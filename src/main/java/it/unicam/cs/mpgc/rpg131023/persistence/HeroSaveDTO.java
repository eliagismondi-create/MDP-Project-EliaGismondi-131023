package it.unicam.cs.mpgc.rpg131023.persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Pure Data Transfer Object representing the serializable state of a hero.
 */
public class HeroSaveDTO {
    public int health;
    public int hunger;
    public int xp;
    public int level;
    public Map<String, Integer> activeBuffs;
    public Map<String, Integer> resources;

    public HeroSaveDTO() {
        this.activeBuffs = new HashMap<>();
        this.resources = new HashMap<>();
    }
}
