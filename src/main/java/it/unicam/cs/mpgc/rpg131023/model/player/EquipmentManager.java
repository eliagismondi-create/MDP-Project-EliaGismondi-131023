package it.unicam.cs.mpgc.rpg131023.model.player;

import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Manages active equipment buffs such as weapon durability and armor shielding.
 */
public class EquipmentManager {

    /** Bonus damage granted by an equipped sword. */
    public static final int SWORD_DAMAGE_BONUS = 25;

    private final PropertyChangeSupport support;
    private final Map<BuffType, Integer> activeBuffs = new EnumMap<>(BuffType.class);

    public EquipmentManager(PropertyChangeSupport support) {
        this.support = support;
    }

    /**
     * @return An unmodifiable view of active buffs.
     */
    public Map<BuffType, Integer> getActiveBuffs() {
        return Collections.unmodifiableMap(this.activeBuffs);
    }

    /**
     * Adds or replaces a buff with the given value.
     *
     * @param type  The buff type.
     * @param value The buff value (e.g. durability or shield points).
     */
    public void addBuff(BuffType type, int value) {
        int oldVal = getBuffValue(type);
        this.activeBuffs.put(type, value);
        support.firePropertyChange("buff_" + type.name(), oldVal, value);
    }

    /**
     * @return The value of the given buff, or 0 if not active.
     */
    public int getBuffValue(BuffType type) {
        return this.activeBuffs.getOrDefault(type, 0);
    }

    /**
     * Removes an active buff.
     *
     * @param type The buff type to remove.
     */
    public void removeBuff(BuffType type) {
        int oldVal = getBuffValue(type);
        this.activeBuffs.remove(type);
        support.firePropertyChange("buff_" + type.name(), oldVal, 0);
    }

    /**
     * @return True if the given buff is active with a positive value.
     */
    public boolean hasBuff(BuffType type) {
        return getBuffValue(type) > 0;
    }

    /**
     * Calculates the total damage considering active weapon buffs.
     *
     * @param baseDamage The base damage without buffs.
     * @return The total damage output.
     */
    public int calculateDamage(int baseDamage) {
        int totalDamage = baseDamage;
        if (hasBuff(BuffType.SWORD)) {
            totalDamage += SWORD_DAMAGE_BONUS;
        }
        return totalDamage;
    }

    /**
     * Called when an attack is made to reduce weapon durability.
     */
    public void onAttack() {
        if (hasBuff(BuffType.SWORD)) {
            int durability = getBuffValue(BuffType.SWORD);
            if (durability > 1) {
                addBuff(BuffType.SWORD, durability - 1);
            } else {
                removeBuff(BuffType.SWORD);
            }
        }
    }

    /**
     * Absorbs damage using shield buff. Returns remaining damage after absorption.
     *
     * @param amount The incoming damage amount.
     * @return The damage remaining after shield absorption.
     */
    public int absorbDamage(int amount) {
        int shield = getBuffValue(BuffType.SHIELD);
        if (shield > 0) {
            if (shield >= amount) {
                addBuff(BuffType.SHIELD, shield - amount);
                return 0;
            } else {
                int remainingDamage = amount - shield;
                removeBuff(BuffType.SHIELD);
                return remainingDamage;
            }
        }
        return amount;
    }
}
