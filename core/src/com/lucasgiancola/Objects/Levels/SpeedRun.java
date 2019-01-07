package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Models.GameModel;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.Balls.BallFactory;
import com.lucasgiancola.Objects.BaseObject;
import com.lucasgiancola.Objects.Blocks.Block;
import com.lucasgiancola.Objects.Blocks.BlockFactory;
import com.lucasgiancola.Objects.Blocks.BlockPowerUp;
import com.lucasgiancola.Objects.PowerUps.PowerUp;
import com.lucasgiancola.Objects.PowerUps.SpeedIncreaser;
import com.lucasgiancola.Objects.PowerUps.SuperBall;
import com.lucasgiancola.Objects.Triggers.Destroyer;
import com.lucasgiancola.Objects.Wall;

import java.util.ArrayList;

public class SpeedRun extends Level {
    private boolean isOver = false;

    // Power Up variables
    private ArrayList<PowerUp> powerUps;

    // Level generation variables
    private int maxCols = 7;
    private int currentRow = 0;
    private Block topBlock = null;

    // Ball shooting variables
    private float baseNewBallDeltaTime = 0.3f; // The default time between balls being shot
    private float newBallDeltaTime = baseNewBallDeltaTime;
    private float newBallCurrentTime = 0;

    private ArrayList<BaseObject> objectsToDestroy;

    public SpeedRun(Stage stage, GameModel gameModel) {
        super(stage, gameModel);

        this.objectsToDestroy = new ArrayList<BaseObject>();
        this.powerUps = new ArrayList<PowerUp>();
    }

    @Override
    public void init() {
        this.setObjectSizes();
        this.setupBoundaries();
        this.insertRow();
    }

    /**
     * Will calculate the size of the blocks and the balls based on the width of the screen and how many columns are allowed
     */
    private void setObjectSizes() {
        Block.blockWidth = ((BallShooter.WIDTH - Block.blockOffset) / this.maxCols) - Block.blockOffset;
        Ball.setRadius((Block.blockWidth / 2) / 3);
    }

    @Override
    void setupBoundaries() {
        // Top
        Wall wall = new Wall(this.world, (int) BallShooter.WIDTH * 2, 2);
        wall.setPosition(BallShooter.WIDTH / 2, BallShooter.HEIGHT);
        wall.setName("Top");
        this.stage.addActor(wall);

        // The "wall" at the bottom that will destroy the blocks
        Destroyer blockBreaker = new Destroyer(this.world, (int) BallShooter.WIDTH * 2, 20);
        blockBreaker.setPosition(BallShooter.WIDTH / 2, -((Ball.getRadius() * 2) * 3));
        blockBreaker.setName("bottom");
        this.stage.addActor(blockBreaker);

        // Left
        wall = new Wall(this.world, 2, (int) BallShooter.HEIGHT * 2);
        wall.setPosition(0, BallShooter.HEIGHT / 2);
        wall.setName("Left");
        this.stage.addActor(wall);

        // Right
        wall = new Wall(this.world, 2, (int) BallShooter.HEIGHT * 2);
        wall.setPosition(BallShooter.WIDTH, BallShooter.HEIGHT / 2);
        wall.setName("right");
        this.stage.addActor(wall);
    }

    @Override
    void restart() {
        this.isOver = false;
        this.resetWorld();
        this.setupBoundaries();
        this.objectsToDestroy = new ArrayList<BaseObject>();
        this.removeAllPowerUps();

        this.insertRow();
    }

    /**
     * Will create a new row of blocks
     */
    private void insertRow() {
        Block toInsert = null;

        for(int col = 0; col < this.maxCols; col++) {
            if(MathUtils.randomBoolean(0.5f)) {
                toInsert = this.createBlock(this.currentRow, col);

                this.stage.addActor(toInsert);
            }
        }

        if(toInsert == null) {
            toInsert = createBlock(this.currentRow, MathUtils.random(0, this.maxCols));
        }

        this.topBlock = toInsert;
    }

    /**
     * Will create a new block and position it properly in the game
     * @param row Only used when it is the first row of blocks being placed
     * @param col The column to place it in
     * @return The new block
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

    public void shootBall() {
        Ball b = BallFactory.baseBall(this.world);
        b.setPosition((BallShooter.WIDTH / 2), 0);
        b.setShootAngle(gameModel.getTouchAngle());
        b.launch();

        this.stage.addActor(b);
        this.newBallCurrentTime = 0;
    }

    /**
     * Figures out if a new block should be spawned based on the location of the block that is in the newest row
     * @return Whether or not a new row should spawn
     */
    private boolean shouldCreateRow() {
        return this.topBlock == null || this.topBlock.getY() <= BallShooter.HEIGHT + Block.blockOffset + Block.blockWidth;
    }

    /**
     * Will remove a power up from the level by setting the proper variables back to what they would be without the power up
     * @param toRemove The power up to remove
     */
    private void removePowerUp(PowerUp toRemove) {
        if(toRemove instanceof SpeedIncreaser) {
            float newDeltaTime = this.newBallDeltaTime + ((SpeedIncreaser) toRemove).getSpeedIncrease();
            this.newBallDeltaTime = Math.min(newDeltaTime, this.baseNewBallDeltaTime);
        } else if(toRemove instanceof SuperBall) {
//            this.ballPower /= ((SuperBall) toRemove).getMultiplier();
        }

        this.powerUps.remove(toRemove);
    }

    private void removeAllPowerUps() {
        this.powerUps = new ArrayList<PowerUp>(); // We don't need to worry about removing all the power ups properly since we are resetting back to base

        this.newBallDeltaTime = this.baseNewBallDeltaTime;
    }

    /* Update the level and the world */

    /**
     * After each world step, some bodies may have collided with one another. This method makes sure that when this
     *  happens, they are removed from the ArrayList.
     */
    private void handleDestroyedObjects() {
        if(!this.objectsToDestroy.isEmpty()) {
            for(BaseObject toDestroy: this.objectsToDestroy) {
                if(toDestroy instanceof BlockPowerUp) {
//                    this.appyPowerUp(toDestroy);
                }

                this.removeBody(toDestroy.getBody());
            }

            this.objectsToDestroy = new ArrayList<BaseObject>();
        }
    }

    public void step(final float step, final int velIts, final int posIts) {
        this.world.step(step, velIts, posIts);
    }

    public void update(float delta) {
        this.newBallCurrentTime += delta;

        this.handleDestroyedObjects();

        if(this.isOver) {
            this.restart();
        }

        if(this.shouldCreateRow()) {
            this.insertRow();
        }

        if(this.newBallCurrentTime >= this.newBallDeltaTime) {
            this.shootBall();
        }
    }

    /* METHODS THAT HAVE TO DO WITH PHYSICS CONTACTS */
    private Block getBlockFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Block) {
            return (Block) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Block) {
            return (Block) temp.getBody().getUserData();
        }

        return null;
    }

    private Ball getBallFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Ball) {
            return (Ball) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Ball) {
            return (Ball) temp.getBody().getUserData();
        }

        return null;
    }

    private Destroyer getDestroyerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Destroyer) {
            return (Destroyer) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Destroyer) {
            return (Destroyer) temp.getBody().getUserData();
        }

        return null;
    }

    @Override
    public void beginContact(Contact contact) {
        Ball ball = getBallFromContact(contact);
        Block block = getBlockFromContact(contact);

        // If it's a ball hitting a block
        if(block != null && ball != null) {
            block.hit(ball);

            if(block.shouldDestroy()) {
                if(!this.objectsToDestroy.contains(block)) {
                    this.objectsToDestroy.add(block);
                }
            }

            return;
        }

        Destroyer destroyer = getDestroyerFromContact(contact);

        if(destroyer != null && ball != null) {
            if(!this.objectsToDestroy.contains(ball)) {
                this.objectsToDestroy.add(ball);
            }

            return;
        }

        // Block hitting wall
        if(destroyer != null && block != null) {
            this.isOver = true;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
