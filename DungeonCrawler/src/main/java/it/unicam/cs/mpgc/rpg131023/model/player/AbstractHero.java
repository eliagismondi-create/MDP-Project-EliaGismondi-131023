package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

/**
 * Base logic for player characters.
 * Holds state for inventory, shield, hunger, and experience.
 */
public abstract class AbstractHero extends AbstractCombatant {
    public static final int MAX_SWORD_DURABILITY = 3;
    public static final int EXPLORATION_FATIGUE = 25;
    public static final int MAX_HEALTH = 100;
    public static final int MAX_HUNGER = 100;
    public static final int ARMOR_SHIELD_VALUE = 50;
    public static final int SWORD_DAMAGE_BONUS = 25;

    private int hunger = 0;
    private int xp = 0;
    private int shield = 0;
    private boolean swordEquipped = false;
    private int swordDurability = 0;
    private final Map<ResourceType, Integer> resources;

    public AbstractHero(final CombatStats stats) {
        super(stats);
        this.resources = new EnumMap<>(ResourceType.class);
    }

    /**
     * Stores specific resource quantity in inventory.
     *
     * @param type   Resource variant.
     * @param amount Quantity to add.
     */
    public void addResource(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Resource type cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be greater than zero.");
        }
        this.resources.put(type, this.resources.getOrDefault(type, 0) + amount);
        support.firePropertyChange("resources", null, getResources());
    }

    /**
     * Removes specific resource quantity from inventory.
     *
     * @param type   Resource variant.
     * @param amount Quantity needed.
     * @return True on success.
     */
    public boolean consumeResource(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Resource type cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to consume must be greater than zero.");
        }

        final int currentAmount = this.resources.getOrDefault(type, 0);
        if (currentAmount < amount) {
            return false;
        }

        this.resources.put(type, currentAmount - amount);
        support.firePropertyChange("resources", null, getResources());
        return true;
    }

    /**
     * Fully restores health using a potion.
     */
    public void heal() {
        if (getHealth() == MAX_HEALTH) {
            throw new IllegalStateException("Hero is not wounded.");
        }
        if (!consumeResource(ResourceType.HEALTH_POTION, 1)) {
            throw new IllegalStateException("No health potion in the inventory.");
        }
        setHealth(MAX_HEALTH);
    }

    /**
     * Resets hunger using a food ration.
     */
    public void eat() {
        if (this.hunger == 0) {
            throw new IllegalStateException("Hero is not hungry.");
        }
        if (!consumeResource(ResourceType.FOOD, 1)) {
            throw new IllegalStateException("No food in the inventory.");
        }
        setHunger(0);
    }

    /**
     * Increases hunger from exploration penalty.
     */
    public void sufferFatigue() {
        this.addHunger(EXPLORATION_FATIGUE);
    }

    public void addHunger(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Hunger increase cannot be negative.");
        }
        setHunger(this.hunger + amount);
        if (this.hunger >= MAX_HUNGER) {
            this.setHealth(0); // Death by starvation
        }
    }

    public Map<ResourceType, Integer> getResources() {
        return Collections.unmodifiableMap(this.resources);
    }

    /**
     * Empties resource inventory map content.
     */
    public void clearResources() {
        this.resources.clear();
        support.firePropertyChange("resources", null, getResources());
    }

    public void setResourceForce(ResourceType type, int amount) {
        this.resources.put(type, amount);
        support.firePropertyChange("resources", null, getResources());
    }

    public int getHunger() {
        return this.hunger;
    }

    public void setHunger(int hunger) {
        int old = this.hunger;
        this.hunger = hunger;
        support.firePropertyChange("hunger", old, this.hunger);
    }

    public int getXp() {
        return this.xp;
    }

    public void setXp(int xp) {
        int old = this.xp;
        this.xp = xp;
        support.firePropertyChange("xp", old, this.xp);
    }

    public int getShield() {
        return this.shield;
    }

    public void setShield(int shield) {
        int old = this.shield;
        this.shield = shield;
        support.firePropertyChange("shield", old, this.shield);
    }

    public boolean isSwordEquipped() {
        return this.swordEquipped;
    }

    public void setSwordEquipped(boolean equipped) {
        boolean old = this.swordEquipped;
        this.swordEquipped = equipped;
        support.firePropertyChange("swordEquipped", old, this.swordEquipped);
    }

    public int getSwordDurability() {
        return this.swordDurability;
    }

    public void setSwordDurability(int durability) {
        int old = this.swordDurability;
        this.swordDurability = durability;
        support.firePropertyChange("swordDurability", old, this.swordDurability);
    }

    /**
     * Consumes armor item to increase shield points.
     */
    public void equipArmor() {
        if (!consumeResource(ResourceType.ARMOR, 1)) {
            throw new IllegalStateException("No armor in the inventory.");
        }
        setShield(this.shield + ARMOR_SHIELD_VALUE);
    }

    /**
     * Consumes sword item to equip weapon and reset durability.
     */
    public void equipSword() {
        if (!consumeResource(ResourceType.SWORD, 1)) {
            throw new IllegalStateException("No sword in the inventory.");
        }
        setSwordEquipped(true);
        setSwordDurability(MAX_SWORD_DURABILITY);
    }

    @Override
    public int getDamage() {
        return this.swordEquipped ? super.getDamage() + SWORD_DAMAGE_BONUS : super.getDamage();
    }

    @Override
    public void attack(it.unicam.cs.mpgc.rpg131023.model.combat.Damageable target) {
        super.attack(target);
        if (this.swordEquipped) {
            setSwordDurability(this.swordDurability - 1);
            if (this.swordDurability <= 0) {
                setSwordEquipped(false);
                setSwordDurability(0);
            }
        }
    }

    /**
     * Absorbs damage prioritizing shield over health.
     *
     * @param amount Incoming damage.
     */
    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Damage amount must be greater than zero.");
        }

        if (this.shield > 0) {
            if (this.shield >= amount) {
                setShield(this.shield - amount);
            } else {
                int remainingDamage = amount - this.shield;
                setShield(0);
                super.takeDamage(remainingDamage);
            }
        } else {
            super.takeDamage(amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AbstractHero hero = (AbstractHero) o;
        return getHealth() == hero.getHealth() && getHunger() == hero.getHunger() &&
                getXp() == hero.getXp() && getShield() == hero.getShield() &&
                isSwordEquipped() == hero.isSwordEquipped() && getSwordDurability() == hero.getSwordDurability();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getHealth(), getHunger(), getXp(), getShield(), isSwordEquipped(),
                getSwordDurability());
    }
}
