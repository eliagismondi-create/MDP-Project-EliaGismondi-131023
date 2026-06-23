package it.unicam.cs.mpgc.rpg131023.model.player;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages active equipment buffs such as weapon durability and armor shielding.
 */
public class EquipmentManager {

    private final PropertyChangeSupport support;
    
    // We can store generic buffs here. Key = Buff ID (e.g., "SWORD", "SHIELD")
    // For simplicity, we just store durability/value.
    private final Map<String, Integer> activeBuffs = new HashMap<>();

    public EquipmentManager(PropertyChangeSupport support) {
        this.support = support;
    }

    public Map<String, Integer> getActiveBuffs() {
        return java.util.Collections.unmodifiableMap(this.activeBuffs);
    }

    public void addBuff(String id, int value) {
        int oldVal = getBuffValue(id);
        this.activeBuffs.put(id, value);
        support.firePropertyChange("buff_" + id, oldVal, value);
    }

    public int getBuffValue(String id) {
        return this.activeBuffs.getOrDefault(id, 0);
    }

    public void removeBuff(String id) {
        int oldVal = getBuffValue(id);
        this.activeBuffs.remove(id);
        support.firePropertyChange("buff_" + id, oldVal, 0);
    }

    public boolean hasBuff(String id) {
        return getBuffValue(id) > 0;
    }

    /**
     * Calculates the total damage considering active weapon buffs.
     */
    public int calculateDamage(int baseDamage) {
        int totalDamage = baseDamage;
        if (hasBuff("SWORD")) {
            // Note: SWORD_DAMAGE_BONUS was 25 in AbstractHero
            totalDamage += 25; 
        }
        return totalDamage;
    }

    /**
     * Called when an attack is made to reduce weapon durability.
     */
    public void onAttack() {
        if (hasBuff("SWORD")) {
            int durability = getBuffValue("SWORD");
            if (durability > 1) {
                addBuff("SWORD", durability - 1);
            } else {
                removeBuff("SWORD");
            }
        }
    }

    /**
     * Absorbs damage using shield buff. Returns remaining damage.
     */
    public int absorbDamage(int amount) {
        int shield = getBuffValue("SHIELD");
        if (shield > 0) {
            if (shield >= amount) {
                addBuff("SHIELD", shield - amount);
                return 0;
            } else {
                int remainingDamage = amount - shield;
                removeBuff("SHIELD");
                return remainingDamage;
            }
        }
        return amount;
    }
}
