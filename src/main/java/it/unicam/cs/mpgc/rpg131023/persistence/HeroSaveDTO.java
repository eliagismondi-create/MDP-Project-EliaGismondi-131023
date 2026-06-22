package it.unicam.cs.mpgc.rpg131023.persistence;

import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

/**
 * Data Transfer Object representing the serializable state of a hero.
 */
public class HeroSaveDTO {
    public int health;
    public int hunger;
    public int xp;
    public int level;
    public int shield;
    public boolean swordEquipped;
    public int swordDurability;
    public Map<ResourceType, Integer> resources;

    public HeroSaveDTO() {
        this.resources = new HashMap<>();
    }

    /**
     * Creates a snapshot DTO from the current state of a hero.
     *
     * @param hero The hero to serialize.
     * @return A populated HeroSaveDTO.
     */
    public static HeroSaveDTO fromHero(AbstractHero hero) {
        HeroSaveDTO dto = new HeroSaveDTO();
        dto.health = hero.getHealth();
        dto.hunger = hero.getHunger();
        dto.xp = hero.getXp();
        dto.level = hero.getLevel();
        dto.shield = hero.getShield();
        dto.swordEquipped = hero.isSwordEquipped();
        dto.swordDurability = hero.getSwordDurability();
        
        hero.getResources().forEach((key, value) -> dto.resources.put(key, value));
        
        return dto;
    }

    /**
     * Restores the state of a hero from this DTO.
     *
     * @param hero The hero to restore.
     */
    public void applyToHero(AbstractHero hero) {
        hero.setHealth(this.health);
        hero.setHunger(this.hunger);
        hero.setXp(this.xp);
        hero.setLevel(this.level > 0 ? this.level : 1);
        hero.setShield(this.shield);
        hero.setSwordEquipped(this.swordEquipped);
        hero.setSwordDurability(this.swordDurability);
        
        hero.clearResources();
        this.resources.forEach(hero::setResourceForce);
    }
}
