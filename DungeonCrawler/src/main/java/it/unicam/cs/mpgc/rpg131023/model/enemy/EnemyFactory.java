package it.unicam.cs.mpgc.rpg131023.model.enemy;

import it.unicam.cs.mpgc.rpg131023.model.combat.CombatStats;
import it.unicam.cs.mpgc.rpg131023.utils.StatsLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Factory 100% Data-Driven per la creazione di nemici.
 * <p>
 * Utilizza la Java Reflection per istanziare la classe concreta indicata
 * dal campo {@code className} in {@code stats.json}, eliminando qualsiasi
 * costrutto {@code switch} hardcoded e rispettando l'Open/Closed Principle:
 * aggiungere un nuovo tipo di nemico richiede solo una nuova classe e una
 * nuova riga nel JSON, senza modificare questa factory.
 * </p>
 */
public final class EnemyFactory {

    private EnemyFactory() {
        // Impedisce l'istanziazione
    }

    /**
     * Crea un'istanza di {@link AbstractEnemy} in modo completamente dinamico.
     * <ol>
     * <li>Carica le statistiche tramite {@link StatsLoader}.</li>
     * <li>Legge il {@code className} dal DTO.</li>
     * <li>Risolve la classe a runtime con {@link Class#forName(String)}.</li>
     * <li>Invoca il costruttore {@code (CombatStats)} via Reflection.</li>
     * </ol>
     *
     * @param enemyId L'identificativo del nemico (es. "goblin", "orc").
     * @return Una nuova istanza di {@link AbstractEnemy}.
     * @throws IllegalArgumentException se {@code enemyId} e' nullo o vuoto.
     * @throws IllegalStateException    se il className e' assente, la classe non
     *                                  esiste, o non e' un {@link AbstractEnemy}.
     */
    public static AbstractEnemy create(final String enemyId) {
        if (enemyId == null || enemyId.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID del nemico non puo' essere nullo o vuoto.");
        }

        final CombatStats stats = StatsLoader.getStatsFor(enemyId);

        final String className = stats.getClassName();
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Nessun className definito per il nemico: " + enemyId);
        }

        try {
            final Class<?> clazz = Class.forName(className);

            if (!AbstractEnemy.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException(
                        "La classe " + className + " non e' un sottotipo di AbstractEnemy.");
            }

            final Constructor<?> constructor = clazz.getConstructor(CombatStats.class);

            return (AbstractEnemy) constructor.newInstance(stats);

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "Classe non trovata: " + className, e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "La classe " + className + " non ha un costruttore (CombatStats).", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(
                    "Errore durante l'istanziazione di " + className, e);
        }
    }
}
