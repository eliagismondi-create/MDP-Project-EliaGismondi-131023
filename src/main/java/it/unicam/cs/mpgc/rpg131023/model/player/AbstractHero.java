package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceCollector;

/**
 * Base logic for player characters.
 * Holds state for inventory, shield, hunger, and experience.
 */
public abstract class AbstractHero extends AbstractCombatant implements ResourceCollector {
    public static final int MAX_SWORD_DURABILITY = 3;
    public static final int EXPLORATION_FATIGUE = 25;
    public static final int MAX_HEALTH = 100;
    public static final int MAX_HUNGER = 100;
    public static final int ARMOR_SHIELD_VALUE = 50;
    public static final int SWORD_DAMAGE_BONUS = 25;

    private int hunger = 0;
    private int xp = 0;
    private int level = 1;
    private int shield = 0;
    private boolean swordEquipped = false;
    private int swordDurability = 0;
    private final Map<Item, Integer> inventory;

    public AbstractHero(final CombatStats stats) {
        super(stats);
        this.inventory = new HashMap<>();
    }

    /**
     * Stores specific item quantity in inventory.
     *
     * @param item   Item variant.
     * @param amount Quantity to add.
     */
    @Override
    public void addItem(final Item item, final int amount) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to add must be greater than zero.");
        }
        this.inventory.put(item, this.inventory.getOrDefault(item, 0) + amount);
        support.firePropertyChange("inventory", null, getInventory());
    }

    /**
     * Removes specific item quantity from inventory.
     *
     * @param item   Item variant.
     * @param amount Quantity needed.
     * @return True on success.
     */
    public boolean consumeItem(final Item item, final int amount) {
        if (item == null) {
            throw new NullPointerException("Item cannot be null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to consume must be greater than zero.");
        }

        final int currentAmount = this.inventory.getOrDefault(item, 0);
        if (currentAmount < amount) {
            return false;
        }

        this.inventory.put(item, currentAmount - amount);
        support.firePropertyChange("inventory", null, getInventory());
        return true;
    }

    public void useItem(Item item) {
        if (!consumeItem(item, 1)) {
            throw new IllegalStateException("No " + item.getName() + " in the inventory.");
        }
        item.use(this);
    }

    public void restoreHealth(int amount) {
        int newHealth = Math.min(MAX_HEALTH, getHealth() + amount);
        setHealth(newHealth);
    }

    public void modifyHunger(int amount) {
        setHunger(Math.max(0, this.hunger + amount));
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

    public Map<Item, Integer> getInventory() {
        return Collections.unmodifiableMap(this.inventory);
    }

    /**
     * Empties resource inventory map content.
     */
    public void clearInventory() {
        this.inventory.clear();
        support.firePropertyChange("inventory", null, getInventory());
    }

    public void setItemForce(Item item, int amount) {
        this.inventory.put(item, amount);
        support.firePropertyChange("inventory", null, getInventory());
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

    public void addXp(int amount) {
        if (amount <= 0) {
            return;
        }
        int totalXp = this.xp + amount;
        int levelsGained = totalXp / 100;
        int remainingXp = totalXp % 100;

        if (levelsGained > 0) {
            setLevel(this.level + levelsGained);
        }
        setXp(remainingXp);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        int old = this.level;
        this.level = level;
        support.firePropertyChange("level", old, this.level);
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

    // Removed specific equip methods. Items handle their own effects via the use() method.

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
                getXp() == hero.getXp() && getLevel() == hero.getLevel() && getShield() == hero.getShield() &&
                isSwordEquipped() == hero.isSwordEquipped() && getSwordDurability() == hero.getSwordDurability();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getHealth(), getHunger(), getXp(), getLevel(), getShield(), isSwordEquipped(),
                getSwordDurability());
    }
}
