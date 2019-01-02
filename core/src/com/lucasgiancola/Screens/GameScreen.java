package com.lucasgiancola.Screens;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Ball;
import com.lucasgiancola.Objects.Block;
import com.lucasgiancola.Objects.Level;
import com.lucasgiancola.Models.GameModel;
import com.lucasgiancola.Objects.Wall;

import java.util.ArrayList;

public class GameScreen extends AbstractScreen implements ContactListener {
    private Level level;
    private ShapeRenderer shapeRenderer;
    private GameModel gameModel;
    private World world;
    private float dtAccumulator = 0f;

    private int cols = 8;
    private int rows = 0;

    private ArrayList<Block> blocksToRemove;
    private ArrayList<Ball> ballsToRemove;

    public GameScreen(BallShooter ballShooter) {
        super(ballShooter);

        this.shapeRenderer = new ShapeRenderer();
        this.gameModel = new GameModel();
        this.blocksToRemove = new ArrayList<Block>();
        this.ballsToRemove = new ArrayList<Ball>();
    }

    @Override
    public void show() {
        super.show();

        this.world = new World(new Vector2(0, 0), true);
        this.world.setContactListener(this);
        World.setVelocityThreshold(0f);

        // Setup everything to do with the board/level
        createLevel();
    }

    private void createLevel() {

        this.initWalls();

        this.initBlocks();

        this.createNewBall();
    }

    // Will initialize all the walls for the game
    private void initWalls() {
        // Top
        Wall wall = new Wall(this.world, (int) BallShooter.WIDTH, 1);
        wall.setPosition(BallShooter.WIDTH / 2, BallShooter.HEIGHT);
        this.stage.addActor(wall);

        // Bottom
        wall = new Wall(this.world, (int) BallShooter.WIDTH, 1);
        wall.setPosition(BallShooter.WIDTH / 2, 0);
        wall.setDestroyer();
        this.stage.addActor(wall);

        // Left
        wall = new Wall(this.world, 1, (int) BallShooter.HEIGHT);
        wall.setPosition(0, BallShooter.HEIGHT / 2);
        this.stage.addActor(wall);

        // Right
        wall = new Wall(this.world, 1, (int) BallShooter.HEIGHT);
        wall.setPosition(BallShooter.WIDTH, BallShooter.HEIGHT / 2);
        this.stage.addActor(wall);
    }

    private void initBlocks() {
        Block.blockWidth = (BallShooter.WIDTH - 8) / cols;
        Ball.setRadius((Block.blockWidth / 2) / 3);

        for(int i = 0; i < rows; i++) {
            insertNewRow(i);
        }
    }

    private void insertNewRow(int currentRow) {
        for(int i = 0; i < cols; i++) {
            Block block = new Block(this.world, Block.blockWidth);
            block.setPosition(i * block.getWidth() + (Block.blockWidth / 2) + 2, BallShooter.HEIGHT - (Block.blockWidth / 2) + (currentRow * Block.blockWidth));
            block.setValue(10);

            this.stage.addActor(block);
        }
    }

    private void createNewBall() {
        Ball b = new Ball(this.world);
        b.setPosition((BallShooter.WIDTH / 2) - Ball.getRadius(), Ball.getRadius() *  2);
        b.setShootAngle(76);
        b.launch();

        this.stage.addActor(b);
    }

    private void updateGame(float delta) {
        System.out.println(this.world.getBodyCount());

        gameModel.update(delta);

        this.removeDestroyedBlocks();
        this.removeDestroyedBalls();

        if(gameModel.instantiateNewBall()) {
            createNewBall();

            gameModel.resetNewBallCounter();
        }

        if(gameModel.instantiateNewRow()) {
            this.insertNewRow(this.rows);
            this.rows++;

            gameModel.resetNewRowCounter();
        }
    }

    private void removeDestroyedBlocks() {
        for(int i = this.blocksToRemove.size() - 1; i >= 0; i--) {
            Block b = this.blocksToRemove.get(i);
            b.remove();
            this.world.destroyBody(b.getBody());

            this.blocksToRemove.remove(b);
        }
    }

    private void removeDestroyedBalls() {
        for(int i = this.ballsToRemove.size() - 1; i >= 0; i--) {
            Ball b = this.ballsToRemove.get(i);
            b.remove();
            this.world.destroyBody(b.getBody());

            this.ballsToRemove.remove(b);
        }
    }

    @Override
    public void render(float delta) {

        // Call box2d with a fixed timestep
        final int VELOCITY_ITERATIONS = 6;
        final int POSITION_ITERATIONS = 2;
        final float FIXED_TIMESTEP = 1.0f / 60.0f;
        this.dtAccumulator += delta;

        while (this.dtAccumulator > FIXED_TIMESTEP) {
            this.dtAccumulator -= FIXED_TIMESTEP;
            if ( this.gameModel.isPlaying() ) {
                this.world.step(FIXED_TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
                updateGame(FIXED_TIMESTEP);
            }
        }

        super.render(delta);

    }

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

    private Wall getWallFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Wall) {
            return (Wall) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Wall) {
            return (Wall) temp.getBody().getUserData();
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

    @Override
    public void beginContact(Contact contact) {

        Ball ball = getBallFromContact(contact);
        Block block = getBlockFromContact(contact);

        // If it's a ball hitting a block
        if(block != null && ball != null) {
            block.hit(ball);

            if(block.shouldDestroy()) {
                blocksToRemove.add(block);
            }

            return;
        }

        Wall wall = getWallFromContact(contact);

        // If it's a ball hitting a wall
        if(wall != null && wall.isDestroyer() && ball != null) {
            ballsToRemove.add(ball);

            return;
        }

        if(wall != null && wall.isDestroyer() && block != null) {
            blocksToRemove.add(block);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold mf) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}