package it.unicam.cs.mpgc.rpg131023.persistence;

import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;

/**
 * Mapper responsible for converting between AbstractHero and HeroSaveDTO.
 */
public class HeroMapper {

    /**
     * Creates a snapshot DTO from the current state of a hero.
     *
     * @param hero The hero to serialize.
     * @return A populated HeroSaveDTO.
     */
    public static HeroSaveDTO toDTO(AbstractHero hero) {
        HeroSaveDTO dto = new HeroSaveDTO();
        dto.health = hero.getHealth();
        dto.hunger = hero.getHunger();
        dto.xp = hero.getXp();
        dto.level = hero.getLevel();
        
        dto.activeBuffs.putAll(hero.getEquipment().getActiveBuffs());
        
        hero.getInventory().forEach((item, amount) -> dto.resources.put(item.getId(), amount));
        
        return dto;
    }

    /**
     * Restores the state of a hero from a DTO.
     *
     * @param hero The hero to restore.
     * @param dto  The DTO containing saved data.
     */
    public static void updateHeroFromDTO(AbstractHero hero, HeroSaveDTO dto) {
        hero.setHealth(dto.health);
        hero.setHunger(dto.hunger);
        hero.setXp(dto.xp);
        hero.setLevel(dto.level > 0 ? dto.level : 1);
        
        // Restore buffs
        dto.activeBuffs.forEach((id, value) -> hero.getEquipment().addBuff(id, value));
        
        // Restore inventory
        hero.clearInventory();
        dto.resources.forEach((id, amount) -> {
            it.unicam.cs.mpgc.rpg131023.model.item.Item item = ItemRegistry.get(id);
            if (item != null) {
                hero.setItemForce(item, amount);
            }
        });
    }
}
