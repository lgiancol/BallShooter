package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.PowerUps.PowerUp;
import com.lucasgiancola.PowerUps.SpeedIncreaser;
import com.lucasgiancola.PowerUps.SuperBall;

import java.util.ArrayList;

public class GameModel {
    private boolean gameInProgress = false;

    private Vector2 pivot;
    private Vector2 touch = null;

    // Power-up variables
    private ArrayList<PowerUp> powerUps;

    // When to create a new ball
    private float newBallCounter = 0f;
    private float baseDeltaTimeNewBall = 0.15f;
    private float currentDeltaTimeNewBall = baseDeltaTimeNewBall;
    private int baseBallPower = 1;
    private int ballPower = baseBallPower;

    // When to add a new row of blocks
    private float newRowCounter = 0f;
    private float deltaTimeNewRow = 1f;

    private Stage stage = null;

    public GameModel() {
        this.gameInProgress = true;

        this.pivot = new Vector2(BallShooter.WIDTH / 2, 0);
        this.powerUps = new ArrayList<PowerUp>();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Update anything that has to do with the model of the game
    public void update(float delta) {
        this.newBallCounter += delta;
        this.newRowCounter += delta;

        this.updatePowerUps(delta);

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

    private void updatePowerUps(float delta) {
        System.out.println("Power Ups: " + this.powerUps.size() + " | Speed: " + this.currentDeltaTimeNewBall);
        for(int i = this.powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = this.powerUps.get(i);
            p.update(delta);

            if(!p.isActive) {
                this.removePowerUp(p);
            }
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

    public boolean instantiateNewBall() {
        return this.newBallCounter >= this.currentDeltaTimeNewBall;
    }

    public int getBallPower() {
        return ballPower;
    }

    public void resetNewBallCounter() {
        this.newBallCounter = 0;
    }

    public boolean instantiateNewRow() {
        return this.newRowCounter >= this.deltaTimeNewRow;
    }

    public void resetNewRowCounter() {
        this.newRowCounter = 0;
    }

    public void addPowerUp(PowerUp toAdd) {

        if(toAdd instanceof SpeedIncreaser) {
            float newDeltaTime = this.currentDeltaTimeNewBall - ((SpeedIncreaser) toAdd).getSpeedIncrease();
            this.currentDeltaTimeNewBall = Math.max(newDeltaTime, 0.05f);
        } else if(toAdd instanceof SuperBall) {
            this.ballPower *= ((SuperBall) toAdd).getMultiplier();
        }

        toAdd.activate();
        this.powerUps.add(toAdd);
    }

    public void removePowerUp(PowerUp toRemove) {
        if(toRemove instanceof SpeedIncreaser) {
            float newDeltaTime = this.currentDeltaTimeNewBall + ((SpeedIncreaser) toRemove).getSpeedIncrease();
            this.currentDeltaTimeNewBall = Math.min(newDeltaTime, this.baseDeltaTimeNewBall);
        } else if(toRemove instanceof SuperBall) {
            this.ballPower /= ((SuperBall) toRemove).getMultiplier();
        }

        this.powerUps.remove(toRemove);
    }

    public void restart() {
        this.powerUps.removeAll(this.powerUps);

        this.currentDeltaTimeNewBall = this.baseDeltaTimeNewBall;
        this.ballPower = this.baseBallPower;
    }
}
