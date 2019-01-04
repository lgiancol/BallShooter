package com.lucasgiancola.PowerUps;

public class PowerUp {
    public boolean isActive = false;
    private float currentRunTime = 0;
    protected float duration = 0;

    public void activate() {
        this.isActive = true;
    }

    public void deactive() {
        this.isActive = false;
    }

    public void update(float delta) {
        this.currentRunTime += delta;

        if(this.currentRunTime >= this.duration) {
            this.deactive();
        }
    }

}
