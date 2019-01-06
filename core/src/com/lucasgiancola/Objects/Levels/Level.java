package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Models.GameModel;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.BaseObject;
import com.lucasgiancola.Objects.Blocks.*;
import com.lucasgiancola.Objects.Triggers.Destroyer;
import com.lucasgiancola.Objects.Wall;

import java.util.ArrayList;

public abstract class Level implements ContactListener {
    protected Stage stage;
    protected GameModel gameModel;

    // Physics variables
    protected World world = null;
    protected ArrayList<BaseObject> objectsToDestroy;

    // Level variables
    protected boolean isOver = false;
    private boolean isLoaded = false;
    protected int maxCols = 8;
    protected int maxRows = -1; // -1 means that there is not a set number of rows
    protected int currentRow = 0;
    protected float runningTime = 0;

    protected Block topBlock = null;

    public static ShapeRenderer renderer = new ShapeRenderer();

    public Level(Stage stage, GameModel gameModel) {
        this.stage = stage;
        this.gameModel = gameModel;

        objectsToDestroy = new ArrayList<BaseObject>();

        initPhysicsWorld();
    }

    private void initPhysicsWorld() {
        Vector2 gravity = new Vector2(0 ,0); // No gravity

        world = new World(gravity, true);
        World.setVelocityThreshold(0f);
        world.setContactListener(this);
    }

    // INITIALIZATION AND RESETTING OF A LEVEL
    public void init() {
        this.setObjectSizes();
        this.setupWallBoundaries();
        this.restartLevel();
    }

    /*
        Will set the size of the blocks and the balls based on how many columns this level will have
     */
    private void setObjectSizes() {
        Block.blockWidth = ((BallShooter.WIDTH - Block.blockOffset) / this.maxCols) - Block.blockOffset;
        Ball.setRadius((Block.blockWidth / 2) / 3);
    }

    /*
        Will create all the walls for the game and place them into the physics world.
     */
    private void setupWallBoundaries() {
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

    /*
        Will reset the level and start from the beginning
     */
    protected abstract void restartLevel();

    /*
        Will remove all blocks and balls from the world
     */
    protected void resetPhysicsWorld() {
        // If for some reason the world is not created yet, create it and return
        if(this.world == null) {
            this.initPhysicsWorld();
            return;
        }

        // Store all bodies in an array
        Array<Body> bodies = new Array<Body>();
        this.world.getBodies(bodies);

        for(Body b: bodies) {
            this.destroyBody(b, false);
        }

//        this.gameModel.restart();
    }

    /*
        All bodies that are currently in the list to be destroyed will be removed
     */
    protected void clearDeadBodies() {
        // Remove all all objects that need to be destroyed
        this.objectsToDestroy.removeAll(this.objectsToDestroy);
        this.currentRow = 0;
    }

    /*
        Sets what happens when the level is over
     */
    protected void levelOver() {
        this.restartLevel();
    }

    // RUNNING OF A LEVEL
    public void update(float delta) {
        if(this.isOver) {
            this.levelOver();
            return;
        }
        this.runningTime += delta;

        this.destroyBodies();
    }

    /*
        Will do a physics step of the world for all the bodies
     */
    public void step(final float timestep, final int velIts, final int posIts) {
        this.world.step(timestep, velIts, posIts);
    }

    /*
        Uses the list of bodies that need to be destroyed, and will destroy them so they are no longer in the world
     */
    protected abstract void destroyBodies();

    /*
        Will destroy a single body in the world. Most of the time this is used to remove blocks or balls from the world,
         but may be used to destroy any physics body.

        @param force: If this is true, it will remove any physics body
     */
    protected void destroyBody(Body toDestroy, boolean force) {
        Object userData = toDestroy.getUserData();

        // If body is BaseObject and we are force destroying it, or it is a Wall or Destroyer
        if(userData instanceof BaseObject && (force || !(userData instanceof Wall || userData instanceof Destroyer))) {
            ((BaseObject) userData).dispose();
            this.world.destroyBody(toDestroy);
        }
    }

    // CONTACT LISTENER FOR THE WORLD
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
    public void preSolve(Contact contact, Manifold mf) {}
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}