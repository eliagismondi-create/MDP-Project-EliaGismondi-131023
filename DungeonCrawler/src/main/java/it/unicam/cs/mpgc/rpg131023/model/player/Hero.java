package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Hero extends AbstractCombatant {
    public static final int MAX_SWORD_DURABILITY = 3;
    public static final int EXPLORATION_FATIGUE = 25;
    public static final int MAX_HEALTH = 100;
    public static final int MAX_HUNGER = 100;
    public static final int ARMOR_SHIELD_VALUE = 50;
    public static final int SWORD_DAMAGE_BONUS = 25;

    private final IntegerProperty hunger = new SimpleIntegerProperty(0);
    private final IntegerProperty xp = new SimpleIntegerProperty(0);
    private final IntegerProperty shield = new SimpleIntegerProperty(0);
    private final BooleanProperty swordEquipped = new SimpleBooleanProperty(false);
    private final IntegerProperty swordDurability = new SimpleIntegerProperty(0);
    private final ObservableMap<ResourceType, Integer> resources;

    public Hero(final CombatStats stats) {
        super(stats);
        this.resources = FXCollections.observableMap(new EnumMap<>(ResourceType.class));
    }

    /**
     * Aggiunge una determinata quantita' di una risorsa all'inventario.
     * 
     * @param type   Il tipo di risorsa.
     * @param amount La quantita' da aggiungere (deve essere strettamente positiva).
     */
    public void addResource(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Il tipo di risorsa non puo' essere null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' da aggiungere deve essere maggiore di zero.");
        }
        this.resources.put(type, this.resources.getOrDefault(type, 0) + amount);
    }

    /**
     * Consuma una risorsa se presente in quantita' sufficiente.
     * 
     * @param type   Il tipo di risorsa.
     * @param amount La quantita' da consumare (deve essere strettamente positiva).
     * @return true se consumata con successo, false se non c'e' abbastanza risorsa.
     */
    public boolean consumeResource(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Il tipo di risorsa non puo' essere null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' da consumare deve essere maggiore di zero.");
        }

        final int currentAmount = this.resources.getOrDefault(type, 0);
        if (currentAmount < amount) {
            return false;
        }

        this.resources.put(type, currentAmount - amount);
        return true;
    }

    /**
     * Cura l'eroe riportando la salute al massimo, consumando una HEALTH_POTION.
     *
     * @throws IllegalStateException se l'eroe ha gia' la salute piena
     *                               o non possiede pozioni curative.
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
     * L'eroe mangia consumando una risorsa FOOD, azzerando la fame.
     *
     * @throws IllegalStateException se l'eroe non ha fame
     *                               o non possiede cibo nell'inventario.
     */
    public void eat() {
        if (this.hunger.get() == 0) {
            throw new IllegalStateException("Hero is not hungry.");
        }
        if (!consumeResource(ResourceType.FOOD, 1)) {
            throw new IllegalStateException("No food in the inventory.");
        }
        this.hunger.set(0);
    }

    /**
     * Applica la penalita' di fame dovuta all'esplorazione del dungeon.
     */
    public void sufferFatigue() {
        this.addHunger(EXPLORATION_FATIGUE);
    }

    /**
     * Aumenta la fame dell'eroe. Se raggiunge 100, l'eroe muore di inedia.
     *
     * @param amount Quantita' di fame da aggiungere (positiva).
     */
    public void addHunger(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("L'aumento di fame non puo' essere negativo.");
        }
        this.hunger.set(this.hunger.get() + amount);
        if (this.hunger.get() >= MAX_HUNGER) {
            this.setHealth(0); // Morte per inedia
        }
    }

    /**
     * Restituisce una copia in sola lettura dell'inventario.
     * Rispetta l'incapsulamento impedendo modifiche non autorizzate
     * (Immutabilita').
     * 
     * @return Mappa immutabile delle risorse.
     */
    public Map<ResourceType, Integer> getResources() {
        return Collections.unmodifiableMap(this.resources);
    }

    public ObservableMap<ResourceType, Integer> resourcesProperty() {
        return this.resources;
    }

    public int getHunger() {
        return this.hunger.get();
    }

    public IntegerProperty hungerProperty() {
        return this.hunger;
    }

    public int getXp() {
        return this.xp.get();
    }

    public IntegerProperty xpProperty() {
        return this.xp;
    }

    public int getShield() {
        return this.shield.get();
    }

    public IntegerProperty shieldProperty() {
        return this.shield;
    }

    public boolean isSwordEquipped() {
        return this.swordEquipped.get();
    }

    public BooleanProperty swordEquippedProperty() {
        return this.swordEquipped;
    }

    public int getSwordDurability() {
        return this.swordDurability.get();
    }

    public IntegerProperty swordDurabilityProperty() {
        return this.swordDurability;
    }

    public void equipArmor() {
        if (!consumeResource(ResourceType.ARMOR, 1)) {
            throw new IllegalStateException("Nessuna armatura nell'inventario.");
        }
        this.shield.set(this.shield.get() + ARMOR_SHIELD_VALUE);
    }

    public void equipSword() {
        if (!consumeResource(ResourceType.SWORD, 1)) {
            throw new IllegalStateException("Nessuna spada nell'inventario.");
        }
        this.swordEquipped.set(true);
        this.swordDurability.set(MAX_SWORD_DURABILITY);
    }

    @Override
    public int getDamage() {
        return this.swordEquipped.get() ? super.getDamage() + SWORD_DAMAGE_BONUS : super.getDamage();
    }

    @Override
    public void attack(it.unicam.cs.mpgc.rpg131023.model.combat.Damageable target) {
        super.attack(target);
        if (this.swordEquipped.get()) {
            this.swordDurability.set(this.swordDurability.get() - 1);
            if (this.swordDurability.get() <= 0) {
                this.swordEquipped.set(false);
                this.swordDurability.set(0);
            }
        }
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' di danni deve essere maggiore di zero.");
        }
        
        if (this.shield.get() > 0) {
            if (this.shield.get() >= amount) {
                this.shield.set(this.shield.get() - amount);
            } else {
                int remainingDamage = amount - this.shield.get();
                this.shield.set(0);
                super.takeDamage(remainingDamage);
            }
        } else {
            super.takeDamage(amount);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return getHealth() == hero.getHealth() && getHunger() == hero.getHunger() &&
               getXp() == hero.getXp() && getShield() == hero.getShield() &&
               isSwordEquipped() == hero.isSwordEquipped() && getSwordDurability() == hero.getSwordDurability();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getHealth(), getHunger(), getXp(), getShield(), isSwordEquipped(), getSwordDurability());
    }
}
