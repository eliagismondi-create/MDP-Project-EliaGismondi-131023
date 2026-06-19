package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

public class Hero extends AbstractCombatant {
    public static final int MAX_SWORD_DURABILITY = 3;

    private int hunger;
    private int xp;
    private int shield = 0;
    private boolean swordEquipped = false;
    private int swordDurability = 0;
    private final Map<ResourceType, Integer> resources;

    public Hero(final CombatStats stats) {
        super(stats);
        this.xp = 0;
        this.hunger = 0;
        this.resources = new EnumMap<>(ResourceType.class);
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
        if (getHealth() == 100) {
            throw new IllegalStateException("Hero is not wounded.");
        }
        if (!consumeResource(ResourceType.HEALTH_POTION, 1)) {
            throw new IllegalStateException("No health potion in the inventory.");
        }
        setHealth(100);
    }

    /**
     * L'eroe mangia consumando una risorsa FOOD, azzerando la fame.
     *
     * @throws IllegalStateException se l'eroe non ha fame
     *                               o non possiede cibo nell'inventario.
     */
    public void eat() {
        if (this.hunger == 0) {
            throw new IllegalStateException("Hero is not hungry.");
        }
        if (!consumeResource(ResourceType.FOOD, 1)) {
            throw new IllegalStateException("No food in the inventory.");
        }
        this.hunger = 0;
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
        this.hunger += amount;
        if (this.hunger >= 100) {
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

    public int getHunger() {
        return this.hunger;
    }

    public int getXp() {
        return this.xp;
    }

    public int getShield() {
        return this.shield;
    }

    public boolean isSwordEquipped() {
        return this.swordEquipped;
    }

    public int getSwordDurability() {
        return this.swordDurability;
    }

    public void equipArmor() {
        if (!consumeResource(ResourceType.ARMOR, 1)) {
            throw new IllegalStateException("Nessuna armatura nell'inventario.");
        }
        this.shield += 50;
    }

    public void equipSword() {
        if (!consumeResource(ResourceType.SWORD, 1)) {
            throw new IllegalStateException("Nessuna spada nell'inventario.");
        }
        this.swordEquipped = true;
        this.swordDurability = MAX_SWORD_DURABILITY;
    }

    @Override
    public int getDamage() {
        return this.swordEquipped ? super.getDamage() + 25 : super.getDamage();
    }

    @Override
    public void attack(it.unicam.cs.mpgc.rpg131023.model.combat.Damageable target) {
        super.attack(target);
        if (this.swordEquipped) {
            this.swordDurability--;
            if (this.swordDurability <= 0) {
                this.swordEquipped = false;
                this.swordDurability = 0;
            }
        }
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' di danni deve essere maggiore di zero.");
        }
        
        if (this.shield > 0) {
            if (this.shield >= amount) {
                this.shield -= amount;
            } else {
                int remainingDamage = amount - this.shield;
                this.shield = 0;
                super.takeDamage(remainingDamage);
            }
        } else {
            super.takeDamage(amount);
        }
    }
}
