package it.unicam.cs.mpgc.rpg131023.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class Hero implements Damageable {
    private int health;
    private int xp;
    private final int damage;
    private final Map<ResourceType, Integer> resources;

    public Hero(final CombatStats stats) {
        if (stats == null) {
            throw new NullPointerException("Le statistiche di combattimento non possono essere null.");
        }
        this.health = stats.getHealth();
        this.xp = 0;
        this.damage = stats.getDamage();
        this.resources = new EnumMap<>(ResourceType.class);
    }

    /**
     * Aggiunge una determinata quantita' di una risorsa all'inventario.
     * 
     * @param type Il tipo di risorsa.
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
     * @param type Il tipo di risorsa.
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
     * Restituisce una copia in sola lettura dell'inventario.
     * Rispetta l'incapsulamento impedendo modifiche non autorizzate (Immutabilita').
     * 
     * @return Mappa immutabile delle risorse.
     */
    public Map<ResourceType, Integer> getResources() {
        return Collections.unmodifiableMap(this.resources);
    }

    public void attack(Damageable target) {
        if (!isAlive())
            throw new IllegalStateException("Hero is dead and cannot attack");
        if (!target.isAlive())
            throw new IllegalArgumentException("Target is already dead");

        target.takeDamage(this.damage);
    }

    @Override
    public void takeDamage(int amount) {
        if (!isAlive())
            throw new IllegalStateException("Hero is already dead");

        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Metodi getter

    public boolean isAlive() {
        return this.health > 0;
    }

    public int getHealth() {
        return this.health;
    }

    public int getXp() {
        return this.xp;
    }

    public int getDamage() {
        return this.damage;
    }
}
