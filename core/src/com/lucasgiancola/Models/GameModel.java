package com.lucasgiancola.Models;

public class GameModel {
    private boolean gameInProgress = false;

    public GameModel() {
        this.gameInProgress = true;
    }

    // Update anything that has to do with the model of the game
    public void update(float delta) {

    }

    public boolean isPlaying() { return gameInProgress; }
}
