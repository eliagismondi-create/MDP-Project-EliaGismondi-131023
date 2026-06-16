package it.unicam.cs.mpgc.rpg131023.model.player;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.model.combat.AbstractCombatant;
import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.model.resource.ResourceType;

public class Hero extends AbstractCombatant {
    private int hunger;
    private int xp;
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
}
