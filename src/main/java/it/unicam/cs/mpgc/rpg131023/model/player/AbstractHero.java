package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.item.Item;
import it.unicam.cs.mpgc.rpg131023.model.item.ItemConsumer;
import it.unicam.cs.mpgc.rpg131023.model.item.Sword;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceCollector;

/**
 * Base logic for player characters.
 * Logic is decoupled into Inventory, LevelSystem, HungerSystem, and EquipmentManager.
 */
public abstract class AbstractHero extends AbstractCombatant implements ResourceCollector, ItemConsumer {
    public static final int MAX_HEALTH = 100;
    
    private final Inventory inventory;
    private final LevelSystem levelSystem;
    private final HungerSystem hungerSystem;
    private final EquipmentManager equipmentManager;

    public AbstractHero(final CombatStats stats) {
        super(stats);
        this.inventory = new Inventory(support);
        this.levelSystem = new LevelSystem(support);
        this.hungerSystem = new HungerSystem(support, () -> this.setHealth(0));
        this.equipmentManager = new EquipmentManager(support);
    }

    @Override
    public EquipmentManager getEquipment() {
        return this.equipmentManager;
    }

    @Override
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public void addItem(final Item item, final int amount) {
        this.inventory.addItem(item, amount);
    }

    public boolean consumeItem(final Item item, final int amount) {
        return this.inventory.consumeItem(item, amount);
    }

    public void useItem(Item item) {
        if (!consumeItem(item, 1)) {
            throw new IllegalStateException("No " + item.getName() + " in the inventory.");
        }
        item.use(this);
    }

    @Override
    public void restoreHealth(int amount) {
        int newHealth = Math.min(MAX_HEALTH, getHealth() + amount);
        setHealth(newHealth);
    }

    @Override
    public void modifyHunger(int amount) {
        this.hungerSystem.modifyHunger(amount);
    }

    public void sufferFatigue() {
        this.hungerSystem.sufferFatigue();
    }

    public void addHunger(int amount) {
        this.hungerSystem.addHunger(amount);
    }

    @Override
    public int getHunger() {
        return this.hungerSystem.getHunger();
    }

    public void setHunger(int hunger) {
        this.hungerSystem.setHunger(hunger);
    }

    public Map<Item, Integer> getInventory() {
        return this.inventory.getItems();
    }

    public void clearInventory() {
        this.inventory.clear();
    }

    public void setItemForce(Item item, int amount) {
        this.inventory.setItemForce(item, amount);
    }

    public int getXp() {
        return this.levelSystem.getXp();
    }

    public void setXp(int xp) {
        this.levelSystem.setXp(xp);
    }

    public void addXp(int amount) {
        this.levelSystem.addXp(amount);
    }

    public int getLevel() {
        return this.levelSystem.getLevel();
    }

    public void setLevel(int level) {
        this.levelSystem.setLevel(level);
    }

    public int getShield() {
        return this.equipmentManager.getBuffValue(BuffType.SHIELD);
    }

    public void setShield(int shield) {
        this.equipmentManager.addBuff(BuffType.SHIELD, shield);
    }

    public boolean isSwordEquipped() {
        return this.equipmentManager.hasBuff(BuffType.SWORD);
    }

    public void setSwordEquipped(boolean equipped) {
        if (equipped) {
            this.equipmentManager.addBuff(BuffType.SWORD, Sword.DEFAULT_DURABILITY);
        } else {
            this.equipmentManager.removeBuff(BuffType.SWORD);
        }
    }

    public int getSwordDurability() {
        return this.equipmentManager.getBuffValue(BuffType.SWORD);
    }

    public void setSwordDurability(int durability) {
        if (durability > 0) {
            this.equipmentManager.addBuff(BuffType.SWORD, durability);
        } else {
            this.equipmentManager.removeBuff(BuffType.SWORD);
        }
    }

    @Override
    public int getDamage() {
        return this.equipmentManager.calculateDamage(super.getDamage());
    }

    @Override
    public void attack(it.unicam.cs.mpgc.rpg131023.model.combat.Damageable target) {
        super.attack(target);
        this.equipmentManager.onAttack();
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Damage amount must be greater than zero.");
        }
        int remainingDamage = this.equipmentManager.absorbDamage(amount);
        if (remainingDamage > 0) {
            super.takeDamage(remainingDamage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
