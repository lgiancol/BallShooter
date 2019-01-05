package com.lucasgiancola.Objects.PowerUps;

public class SuperBall extends PowerUp {
    private int multiplier;

    public SuperBall(int hitMultiplier, float duration) {
        this.duration = duration;
        this.multiplier = hitMultiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
