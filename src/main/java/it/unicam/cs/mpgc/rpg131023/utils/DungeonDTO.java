package it.unicam.cs.mpgc.rpg131023.utils;

import java.util.Map;

/**
 * Data Transfer Object for JSON parsing of dungeons.
 */
public class DungeonDTO {
    public String name;
    public String description;
    public String difficulty;
    public Map<String, Integer> loot;
    public Map<String, Integer> enemySpawns;
}

