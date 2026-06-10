package it.unicam.cs.mpgc.rpg131023.model;

// Value Object immutabile che racchiude le statistiche di combattimento di base.
public final class CombatStats {
    private final int health;
    private final int damage;

    /**
     * Costruisce le statistiche validando i limiti.
     * 
     * @param health
     * @param damage
     */
    public CombatStats(final int health, final int damage) {
        if (health <= 0 || health > 100) {
            throw new IllegalArgumentException("La salute deve essere compresa tra 1 e 100 inclusi.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Il danno non puo' essere negativo.");
        }
        this.health = health;
        this.damage = damage;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }
}
