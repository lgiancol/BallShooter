package com.lucasgiancola.Models;

public class GameModel {
    private boolean gameInProgress = false;
    private float currentDeltaTime = 0f;
    private float deltaTimeNewBall = 0.2f;

    public GameModel() {
        this.gameInProgress = true;
    }

    // Update anything that has to do with the model of the game
    public void update(float delta) {
        currentDeltaTime += delta;
    }

    public boolean isPlaying() { return gameInProgress; }

    public boolean instantiateNewBall() {
        return this.currentDeltaTime >= deltaTimeNewBall;
    }

    public void resetCurrentTime() {
        this.currentDeltaTime = 0;
    }
}
