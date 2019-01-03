package com.lucasgiancola.Models;

public class GameModel {
    private boolean gameInProgress = false;

    // When to create a new ball
    private float newBallCounter = 0f;
    private float deltaTimeNewBall = 0.05f;

    // When to add a new row of blocks
    private float newRowCounter = 0f;
    private float deltaTimeNewRow = 1f;

    public GameModel() {
        this.gameInProgress = true;
    }

    // Update anything that has to do with the model of the game
    public void update(float delta) {
        newBallCounter += delta;
        newRowCounter += delta;
    }

    public boolean isPlaying() { return gameInProgress; }

    public boolean instantiateNewBall() {
        return this.newBallCounter >= this.deltaTimeNewBall;
    }

    public void resetNewBallCounter() {
        this.newBallCounter = 0;
    }

    public boolean instantiateNewRow() {
//        return false;
        return this.newRowCounter >= this.deltaTimeNewRow;
    }

    public void resetNewRowCounter() {
        this.newRowCounter = 0;
    }
}
