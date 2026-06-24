package it.unicam.cs.mpgc.rpg131023.persistence;

import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemRegistry;
import it.unicam.cs.mpgc.rpg131023.model.player.AbstractHero;
import it.unicam.cs.mpgc.rpg131023.model.player.BuffType;

/**
 * Mapper responsible for converting between AbstractHero and HeroSaveDTO.
 */
public class HeroMapper {

    private final ItemRegistry itemRegistry;

    /**
     * Creates a mapper that uses the given item registry for inventory restoration.
     *
     * @param itemRegistry The registry to resolve item IDs.
     */
    public HeroMapper(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    /**
     * Creates a snapshot DTO from the current state of a hero.
     *
     * @param hero The hero to serialize.
     * @return A populated HeroSaveDTO.
     */
    public HeroSaveDTO toDTO(AbstractHero hero) {
        HeroSaveDTO dto = new HeroSaveDTO();
        dto.health = hero.getHealth();
        dto.hunger = hero.getHunger();
        dto.xp = hero.getXp();
        dto.level = hero.getLevel();
        
        hero.getEquipment().getActiveBuffs().forEach(
                (type, value) -> dto.activeBuffs.put(type.name(), value)
        );
        
        hero.getInventory().forEach((item, amount) -> dto.resources.put(item.getId(), amount));
        
        return dto;
    }

    /**
     * Restores the state of a hero from a DTO.
     *
     * @param hero The hero to restore.
     * @param dto  The DTO containing saved data.
     */
    public void updateHeroFromDTO(AbstractHero hero, HeroSaveDTO dto) {
        hero.setHealth(dto.health);
        hero.setHunger(dto.hunger);
        hero.setXp(dto.xp);
        hero.setLevel(dto.level > 0 ? dto.level : 1);
        
        // Restore buffs
        dto.activeBuffs.forEach((id, value) -> {
            BuffType buffType = BuffType.valueOf(id);
            hero.getEquipment().addBuff(buffType, value);
        });
        
        // Restore inventory
        hero.clearInventory();
        dto.resources.forEach((id, amount) -> {
            Item item = itemRegistry.get(id);
            hero.setItemForce(item, amount);
        });
    }
}
