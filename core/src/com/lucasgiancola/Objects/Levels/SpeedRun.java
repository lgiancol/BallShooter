package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private int startingRows = 2;
    private Block topBlock = null;

    // Power-up variables
    private ArrayList<PowerUp> powerUps;

    public SpeedRun(Stage stage, GameModel gameModel) {
        super(stage, gameModel);

        this.powerUps = new ArrayList<PowerUp>();

        helper = new AngleHelper(new Vector2(BallShooter.WIDTH / 2, 0));
        stage.addActor(helper);
    }

    /*
        Makes sure all variables that control state of the level are reset
     */
    protected void restartLevel() {
        this.isOver = false;
        this.currentRow = 0;
        this.topBlock = null;

        this.resetPhysicsWorld();
        this.clearDeadBodies();

        this.removeAllPowerUps();

        for(int i = 0; i < this.startingRows; i++) {
            insertRow();
        }
    }
    // CREATING/MANAGING PHYSICS OBJECTS IN THE WORLD

    /*
        Will insert at least 1 block in a new row
     */
    private void insertRow() {
        this.currentRow++;
        float chance = 0.5f;
        Block toInsert = null;

        for(int col = 0; col < this.maxCols; col++) {
            if(MathUtils.randomBoolean(chance)) {
                toInsert = this.createBlock(this.currentRow, col);

                this.stage.addActor(toInsert);
            }
        }

        if(toInsert == null) {
            int col = MathUtils.random(0, this.maxCols);
            toInsert = this.createBlock(this.currentRow, col);
        }

        this.topBlock = toInsert;
    }

    private boolean shouldCreateRow() {
        return this.topBlock == null || this.topBlock.getY() <= BallShooter.HEIGHT + (this.startingRows * Block.blockOffset) + (this.startingRows + Block.blockWidth);
    }

    /*
        Will create a block at the current row, col location or above the previously placed block
     */
    public Block createBlock(int row, int col) {
        Block block;
        float x = col * Block.blockWidth + (Block.blockWidth / 2) + ((col + 1) * Block.blockOffset);
        float y = BallShooter.HEIGHT + (row * Block.blockWidth) + (row * Block.blockOffset);

        if(this.topBlock != null) {
            y = Constants.boxToPixels(this.topBlock.getBody().getPosition().y) + Block.blockWidth + Block.blockOffset;
        }

        // Create the new Block
        block = BlockFactory.createRandomBlock(this.world);
        block.setLocation(row, col);
        block.setPosition(x, y);

        return block;
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

    // PHYSICS BODIES SECTION
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

    private void applyPowerUp(Object userData) {
        if(userData instanceof BlockPowerUp) {
            this.addPowerUp(((BlockPowerUp) userData).getPowerUp());
        }
    }

    private void removeAllPowerUps() {
        for(int i = this.powerUps.size() - 1; i >= 0; i--) {
            removePowerUp(this.powerUps.get(i));
        }

        this.newBallDeltaTime = this.baseNewBallDeltaTime;
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

        this.updatePowerUps(delta);

        // If the new ball counter is greater than or equal to the time it takes to create a new ball, create a new ball
        if(this.newBallCounter >= this.newBallDeltaTime) {
            this.shootBall();

            this.newBallCounter = 0;
        }

        if(this.shouldCreateRow()) {
            this.insertRow();
        }
    }
}
