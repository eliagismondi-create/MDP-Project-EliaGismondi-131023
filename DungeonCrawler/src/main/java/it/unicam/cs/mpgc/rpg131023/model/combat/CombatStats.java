package it.unicam.cs.mpgc.rpg131023.model.combat;

/**
 * Value Object immutabile che racchiude le statistiche di combattimento di
 * base.
 * Il campo opzionale {@code className} consente alla {@code EnemyFactory} di
 * risolvere
 * la classe concreta del nemico a runtime tramite Reflection (OCP).
 */
public final class CombatStats {
    private final int health;
    private final int damage;
    private final String className;

    /**
     * Costruisce le statistiche validando i limiti.
     *
     * @param health    La salute iniziale (1-100).
     * @param damage    Il danno base (>= 0).
     * @param className Il fully-qualified class name del nemico (può essere null
     *                  per il Hero).
     */
    public CombatStats(final int health, final int damage, final String className) {
        if (health <= 0 || health > 100) {
            throw new IllegalArgumentException("La salute deve essere compresa tra 1 e 100 inclusi.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Il danno non puo' essere negativo.");
        }
        this.health = health;
        this.damage = damage;
        this.className = className;
    }

    /**
     * Costruisce le statistiche senza className (retrocompatibilita').
     *
     * @param health La salute iniziale (1-100).
     * @param damage Il danno base (>= 0).
     */
    public CombatStats(final int health, final int damage) {
        this(health, damage, null);
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }

    /**
     * @return Il fully-qualified class name dell'entita', oppure {@code null} se
     *         non specificato.
     */
    public String getClassName() {
        return this.className;
    }
}
