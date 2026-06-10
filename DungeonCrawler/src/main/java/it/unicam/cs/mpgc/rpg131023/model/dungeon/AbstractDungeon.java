package it.unicam.cs.mpgc.rpg131023.model.dungeon;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import it.unicam.cs.mpgc.rpg131023.EnemyType;
import it.unicam.cs.mpgc.rpg131023.model.ResourceType;

public abstract class AbstractDungeon {
    private final Map<ResourceType, Integer> treasure;
    private final Map<EnemyType, Integer> enemySpawns;

    public AbstractDungeon() {
        this.treasure = new EnumMap<>(ResourceType.class);
        this.enemySpawns = new HashMap<>();
    }

    /**
     * Aggiunge risorse al bottino (loot) del dungeon in modo sicuro (Fail-Fast).
     * 
     * @param type   Il tipo di risorsa.
     * @param amount La quantita' da aggiungere.
     */
    public void addLoot(final ResourceType type, final int amount) {
        if (type == null) {
            throw new NullPointerException("Il tipo di risorsa non puo' essere null.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("La quantita' deve essere maggiore di zero.");
        }
        this.treasure.put(type, this.treasure.getOrDefault(type, 0) + amount);
    }

    /**
     * Configura la quantita' di nemici previsti per questo dungeon.
     * 
     * @param enemyId  L'identificatore testuale del nemico (es. "goblin", "orc").
     * @param quantity Il numero di nemici di quel tipo.
     */
    public void addEnemySpawn(final EnemyType enemyType, final int quantity) {
        if (enemyType == null) {
            throw new NullPointerException("Il tipo di nemico non puo' essere null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantita' deve essere maggiore di zero.");
        }
        this.enemySpawns.put(enemyType, this.enemySpawns.getOrDefault(enemyType, 0) + quantity);
    }

    /**
     * @return Mappa immutabile del tesoro.
     */
    public Map<ResourceType, Integer> getTreasure() {
        return Collections.unmodifiableMap(this.treasure);
    }

    /**
     * @return Mappa immutabile degli spawn dei nemici.
     */
    public Map<EnemyType, Integer> getEnemySpawns() {
        return Collections.unmodifiableMap(this.enemySpawns);
    }
}
