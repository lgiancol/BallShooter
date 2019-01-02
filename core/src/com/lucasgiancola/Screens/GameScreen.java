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

    private ArrayList<Block> blocksToRemove;

    public GameScreen(BallShooter ballShooter) {
        super(ballShooter);

        this.shapeRenderer = new ShapeRenderer();
        this.gameModel = new GameModel();
        this.blocksToRemove = new ArrayList<Block>();

//        level = new Level(camera.viewportWidth, camera.viewportHeight);

//        BallShooter.input.setCam(camera);
//        BallShooter.input.setVp(viewport);
//        Gdx.input.setInputProcessor(BallShooter.input);
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

        for(int i = 0; i < 5; i++) {
            Block block = new Block(this.world);
            block.setPosition(i * block.getWidth() + 400, 300);
            block.setValue(10);
//            block.setRotationSpeed(2);

            this.stage.addActor(block);
        }
        Ball b = new Ball(this.world);
        b.setPosition(400, 300);
        b.setShootAngle(45);
        this.stage.addActor(b);
        b.tempFlick();
    }

    private void initWalls() {
        Wall wall = new Wall(this.world, 400, 2);
        wall.setPosition(500, 500);
        this.stage.addActor(wall);

        wall = new Wall(this.world, 400, 2);
        wall.setPosition(500, 100);
        this.stage.addActor(wall);

        wall = new Wall(this.world, 2, 400);
        wall.setPosition(300, 300);
        this.stage.addActor(wall);

        wall = new Wall(this.world, 2, 400);
        wall.setPosition(700, 300);
        this.stage.addActor(wall);
    }

    private void updateGame(float delta) {

        this.removeDestroyedBlocks();
    }

    private void removeDestroyedBlocks() {
        for(Block b : this.blocksToRemove) {
            this.world.destroyBody(b.getBody());
            b.remove();
        }
        this.blocksToRemove.removeAll(this.blocksToRemove);
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

    @Override
    public void beginContact(Contact contact) {
        Fixture f1 = contact.getFixtureA();
        Fixture f2 = contact.getFixtureB();

        if(f1 != null && f2 != null) {
            Block blockObj = null;
            Ball ballObj = null;
            Object obj = f1.getBody().getUserData();

            // First object is a block, second is a ball
            if (obj != null && obj instanceof Block) {
                blockObj = (Block) obj;

                obj = f2.getBody().getUserData();
                if (obj != null && obj instanceof Ball) {
                    ballObj = (Ball) obj;
                }

            }
            // Second object is a block, first is a ball
            else {
                obj = f2.getBody().getUserData();

                if (obj != null && obj instanceof Block) {
                    blockObj = (Block) obj;

                    obj = f1.getBody().getUserData();
                    if (obj != null && obj instanceof Ball) {
                        ballObj = (Ball) obj;

                        blockObj.hit(ballObj);
                    }
                }
            }

            // If ball hit block, do damage and add it to the remove list if necessary
            if(blockObj != null && ballObj != null) {
                blockObj.hit(ballObj);

                if(blockObj.shouldDestroy()) {
                    blocksToRemove.add(blockObj);
                }
            }
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