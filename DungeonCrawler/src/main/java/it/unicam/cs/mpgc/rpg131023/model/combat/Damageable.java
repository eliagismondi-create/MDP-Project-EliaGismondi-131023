package it.unicam.cs.mpgc.rpg131023.model.combat;

public interface Damageable {

    public void takeDamage(int amount);

    public boolean isAlive();
}
