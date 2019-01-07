package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.BallShooter;

public class GameModel {
    private boolean gameInProgress = false;
    public float runningTime = 0;

    private Vector2 pivot;
    private Vector2 touch = null;

    private Stage stage = null;

    public GameModel() {
        this.gameInProgress = true;

        this.pivot = new Vector2(BallShooter.WIDTH / 2, 0);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Update anything that has to do with the model of the game
    public void update(float delta) {
        this.runningTime += delta;

        if(Gdx.input.isTouched()) {
            this.updateTouchPositions();
        }
    }

    // Will set the touch position based on the stage
    private void updateTouchPositions() {
        Vector2 temp = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        if(this.stage != null) {
            touch = stage.getViewport().unproject(temp);
        }
    }

    public float getTouchAngle() {
        if(touch != null) {
            Vector2 temp = new Vector2(touch.x, touch.y);
            temp.sub(pivot);
            temp.nor();
            return temp.angle();
        }

        return 90;
    }

    public boolean isPlaying() { return gameInProgress; }
}
