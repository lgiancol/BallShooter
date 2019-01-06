package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Models.GameModel;
import com.lucasgiancola.Objects.AngleHelper;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.BaseObject;
import com.lucasgiancola.Objects.Blocks.Block;
import com.lucasgiancola.Objects.Blocks.BlockFactory;
import com.lucasgiancola.Objects.Blocks.BlockPowerUp;
import com.lucasgiancola.Objects.PowerUps.PowerUp;
import com.lucasgiancola.Objects.PowerUps.SpeedIncreaser;
import com.lucasgiancola.Objects.PowerUps.SuperBall;

import java.util.ArrayList;

public class SpeedRun extends Level {
    // Debug variables
    public boolean createBalls = true;
    public boolean createBlocks = true;
    public AngleHelper helper = null;

    // Ball variables
    private float newBallCounter = 0f;
    private float baseNewBallDeltaTime = 0.15f;
    private float newBallDeltaTime = baseNewBallDeltaTime;
    private int baseBallPower = 1;
    private int ballPower = baseBallPower;

    // Row variables
    private float newRowCounter = 0f;
    private float baseNewRowDeltaTime = 1.5f;
    private float newRowDeltaTime = baseNewRowDeltaTime;

    // Power-up variables
    private ArrayList<PowerUp> powerUps;

    public SpeedRun(Stage stage, GameModel gameModel) {
        super(stage, gameModel);

        this.powerUps = new ArrayList<PowerUp>();

        helper = new AngleHelper(new Vector2(BallShooter.WIDTH / 2, 0));
        stage.addActor(helper);
    }

    protected void restartLevel() {
        this.isOver = false;
        this.currentRow = 0;
        this.topBlock = null;

        this.resetPhysicsWorld();
        this.clearDeadBodies();

        this.removeAllPowerUps();

        this.topBlock = insertNewRow();
    }

    private Block insertNewRow() {
        float chance = 0.6f;
        this.currentRow++;
        Block lastInRow = null;

        for(int col = 0; col < this.maxCols; col++) {
            if(MathUtils.randomBoolean(chance)) {
                // Calculate the position of the new block based on the old top block (if there is one)
                float x = col * Block.blockWidth + (Block.blockWidth / 2) + ((col + 1) * Block.blockOffset);
                float y = BallShooter.HEIGHT + (this.currentRow * Block.blockWidth) + (this.currentRow * Block.blockOffset);

                if(this.topBlock != null) {
                    y = Constants.boxToPixels(this.topBlock.getBody().getPosition().y) + Block.blockWidth + Block.blockOffset;
                }

                // Create the new Block
                lastInRow = BlockFactory.createRandomBlock(this.world);
                lastInRow.setLocation(this.currentRow, col);
                lastInRow.setPosition(x, y);

                this.stage.addActor(lastInRow);

//                this.placeBlock(lastInRow, this.currentRow, col);
            }
        }

        if(lastInRow == null) {
            int col = MathUtils.random(0, this.maxCols);
            // Calculate the position of the new block based on the old block (if there is one)
            float x = col * Block.blockWidth + (Block.blockWidth / 2) + ((col + 1) * Block.blockOffset);
            float y = BallShooter.HEIGHT + (this.currentRow * Block.blockWidth) + (this.currentRow * Block.blockOffset);

            if(this.topBlock != null) {
                y = Constants.boxToPixels(this.topBlock.getBody().getPosition().y) + Block.blockWidth + Block.blockOffset;
            }

            lastInRow = BlockFactory.createRandomBlock(this.world);
            lastInRow.setLocation(this.currentRow, col);
            lastInRow.setPosition(x, y);
        }

        return lastInRow;
    }

    // Will create a new ball and place it into the world
    private void shootBall() {
        Ball b = new Ball(this.world);
        b.setPosition((BallShooter.WIDTH / 2), 0);
        b.setHitValue(this.ballPower);
        b.setShootAngle(gameModel.getTouchAngle());
        b.launch();

        this.stage.addActor(b);
    }

    protected void destroyBodies() {
        if(!this.objectsToDestroy.isEmpty()) {
            for(int i = this.objectsToDestroy.size() - 1; i >= 0; i--) {
                BaseObject obj = this.objectsToDestroy.get(i);
                this.destroyBody(obj.getBody(), true);
            }
        }

        this.objectsToDestroy.removeAll(this.objectsToDestroy);
    }

    /*
        Since this level can have power ups, we need to make sure to apply the power up if it is available
     */
    @Override
    protected void destroyBody(Body toDestroy, boolean force) {
        this.applyPowerUp(toDestroy.getUserData());

        super.destroyBody(toDestroy, force);
    }

    private void applyPowerUp(Object userData) {
        if(userData instanceof BlockPowerUp) {
            this.addPowerUp(((BlockPowerUp) userData).getPowerUp());
        }
    }

    // POWER UP SECTION
    /*
        Will add a power up the level
     */
    private void addPowerUp(PowerUp toAdd) {
        if(toAdd instanceof SpeedIncreaser) {
            float newDeltaTime = this.newBallDeltaTime - ((SpeedIncreaser) toAdd).getSpeedIncrease();
            this.newBallDeltaTime = Math.max(newDeltaTime, 0.05f);
        } else if(toAdd instanceof SuperBall) {
            this.ballPower *= ((SuperBall) toAdd).getMultiplier();
        }

        toAdd.activate();
        this.powerUps.add(toAdd);
    }

    /*
        Will remove a power up from a level
     */
    private void removePowerUp(PowerUp toRemove) {
        if(toRemove instanceof SpeedIncreaser) {
            float newDeltaTime = this.newBallDeltaTime + ((SpeedIncreaser) toRemove).getSpeedIncrease();
            this.newBallDeltaTime = Math.min(newDeltaTime, this.baseNewBallDeltaTime);
        } else if(toRemove instanceof SuperBall) {
            this.ballPower /= ((SuperBall) toRemove).getMultiplier();
        }

        this.powerUps.remove(toRemove);
    }

    private void removeAllPowerUps() {
        for(int i = this.powerUps.size() - 1; i >= 0; i--) {
            removePowerUp(this.powerUps.get(i));
        }

        this.newBallDeltaTime = this.baseNewBallDeltaTime;
        this.newRowDeltaTime = this.baseNewRowDeltaTime;
    }

    /*
        Updates a power up so it can be removed it is over
     */
    private void updatePowerUps(float delta) {
        for(int i = this.powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = this.powerUps.get(i);
            p.update(delta);

            if(!p.isActive) {
                this.removePowerUp(p);
            }
        }
    }

    // UPDATE AND RENDER FOR THE LEVEL
    @Override
    public void update(float delta) {
        super.update(delta);

        this.newBallCounter += delta;
        this.newRowCounter += delta;

        this.updatePowerUps(delta);

        // If the new ball counter is greater than or equal to the time it takes to create a new ball, create a new ball
        if(this.newBallCounter >= this.newBallDeltaTime) {
            this.shootBall();

            this.newBallCounter = 0;
        }

        if(this.topBlock.getY() <= BallShooter.HEIGHT + (Block.blockOffset) + Block.blockWidth) {
            this.topBlock = this.insertNewRow();

            this.newRowCounter = 0;
        }
    }
}
