package it.unicam.cs.mpgc.rpg131023.model;

/**
 * Superclasse astratta che centralizza la logica di base per le entita' combattenti,
 * risolvendo la violazione del principio DRY (Don't Repeat Yourself).
 * Implementa le interfacce {@link Damageable} e {@link Attacker}.
 */
public abstract class AbstractCombatant implements Damageable, Attacker {
    private int health;
    private final int damage;

    /**
     * Costruisce un combattente applicando il paradigma Fail-Fast.
     *
     * @param stats Le statistiche di combattimento, non possono essere null.
     */
    public AbstractCombatant(final CombatStats stats) {
        if (stats == null) {
            throw new NullPointerException("Le statistiche di combattimento non possono essere null.");
        }
        this.health = stats.getHealth();
        this.damage = stats.getDamage();
    }

    @Override
    public void attack(Damageable target) {
        if (!isAlive()) {
            throw new IllegalStateException("L'entita' attaccante e' morta e non puo' attaccare.");
        }
        if (target == null) {
            throw new NullPointerException("Il bersaglio dell'attacco non puo' essere null.");
        }
        if (!target.isAlive()) {
            throw new IllegalArgumentException("Il bersaglio e' gia' morto.");
        }

        target.takeDamage(this.damage);
    }

    @Override
    public void takeDamage(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' di danni deve essere maggiore di zero.");
        }
        if (!isAlive()) {
            throw new IllegalStateException("L'entita' e' gia' morta.");
        }

        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    public int getHealth() {
        return this.health;
    }

    public int getDamage() {
        return this.damage;
    }
}
