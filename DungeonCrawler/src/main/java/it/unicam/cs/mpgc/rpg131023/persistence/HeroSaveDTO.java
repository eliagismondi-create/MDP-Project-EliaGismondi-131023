package it.unicam.cs.mpgc.rpg131023.persistence;

import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.player.Hero;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

public class HeroSaveDTO {
    public int health;
    public int hunger;
    public int xp;
    public int shield;
    public boolean swordEquipped;
    public int swordDurability;
    public Map<ResourceType, Integer> resources;

    public HeroSaveDTO() {
        this.resources = new HashMap<>();
    }

    public static HeroSaveDTO fromHero(Hero hero) {
        HeroSaveDTO dto = new HeroSaveDTO();
        dto.health = hero.getHealth();
        dto.hunger = hero.getHunger();
        dto.xp = hero.getXp();
        dto.shield = hero.getShield();
        dto.swordEquipped = hero.isSwordEquipped();
        dto.swordDurability = hero.getSwordDurability();
        
        for (Map.Entry<ResourceType, Integer> entry : hero.getResources().entrySet()) {
            dto.resources.put(entry.getKey(), entry.getValue());
        }
        
        return dto;
    }

    public void applyToHero(Hero hero) {
        // We set properties explicitly to notify JavaFX bindings
        hero.healthProperty().set(this.health);
        hero.hungerProperty().set(this.hunger);
        hero.xpProperty().set(this.xp);
        hero.shieldProperty().set(this.shield);
        hero.swordEquippedProperty().set(this.swordEquipped);
        hero.swordDurabilityProperty().set(this.swordDurability);
        
        hero.resourcesProperty().clear();
        for (Map.Entry<ResourceType, Integer> entry : this.resources.entrySet()) {
            hero.resourcesProperty().put(entry.getKey(), entry.getValue());
        }
    }
}
