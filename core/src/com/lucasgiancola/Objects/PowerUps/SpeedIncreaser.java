package com.lucasgiancola.Objects.PowerUps;

public class SpeedIncreaser extends PowerUp {
    private float speedIncrease;

    public SpeedIncreaser(float speedIncrease, float length) {
        this.speedIncrease = speedIncrease;
        this.duration = length;
    }

    public float getSpeedIncrease() {
        return speedIncrease;
    }
}
