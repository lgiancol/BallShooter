package com.lucasgiancola.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Models.GameModel;

import java.util.ArrayList;

public class Level implements ContactListener {
    private int maxColums = 8;
    private int currentRow = -1;
    private boolean shouldRestart = false;
    private GameModel gameModel;

    // Render variables
    private Stage stage;

    // Physics variables
    private World world = null;
    private ArrayList<BaseObject> objectsToDestroy;

    // Debug variables
    public boolean createBalls = true;
    public boolean createBlocks = true;
    public AngleHelper helper = null;

    public Level(Stage stage, GameModel gameModel) {
        objectsToDestroy = new ArrayList<BaseObject>();
        this.stage = stage;
        this.gameModel = gameModel;

        helper = new AngleHelper(new Vector2(BallShooter.WIDTH / 2, 0));
        stage.addActor(helper);
    }

    public Level(String toLoad, Stage stage) {
        this.stage = stage;
    }

    // Will be called only when gameScreen.show() is called
    public void initLevel() {
        initWorld();
        insertWalls();
        restartLevel();
    }

    // Will create a new world for the objects to live in
    private void initWorld() {
        Vector2 gravity = new Vector2(0 ,0); // No gravity
        world = new World(gravity, true);
        World.setVelocityThreshold(0f);
        world.setContactListener(this);

        // Sets the base Block width and Ball radius for this level
        Block.blockWidth = (BallShooter.WIDTH - 20) / this.maxColums;
        Ball.setRadius((Block.blockWidth / 2) / 3);
    }

    // Will create the walls of the game which won't be remade when a level is restarted
    private void insertWalls() {
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

    // Will reset all the elements inside the world
    public void restartLevel() {
        resetWorld();
        createNewLevel();
    }

    // Will remove all the blocks and balls from the world, but will keep the walls and destroyer
    private void resetWorld() {
        if(this.world == null) {
            initWorld();
            return;
        }

        // Get all bodies into a variable
        Array<Body> bodies = new Array<Body>();
        this.world.getBodies(bodies);

        // Remove all the bodies
        for(Body b: bodies) {
            // If userData is a BaseObject and it is not a Wall or a Destroyer
            destroyBody(b, false);
        }
    }

    // Will properly destroy a body
    private void destroyBody(Body toDestroy, boolean force) {
        Object userData = toDestroy.getUserData();

        // If body is BaseObject and we are force destroying it, or it is a Wall or Destroyer
        if(userData instanceof BaseObject && (force || !(userData instanceof Wall || userData instanceof Destroyer))) {
            ((BaseObject) userData).dispose();
            this.world.destroyBody(toDestroy);
        }
    }

    // Will place all necessary objects into the game world
    public void createNewLevel() {
        // Remove all all objects that need to be destroyed
        this.objectsToDestroy.removeAll(this.objectsToDestroy);
        this.shouldRestart = false;
        this.currentRow = -1;

        initBlocks();
    }

    private void initBlocks() {
        int startingRows = 2;

        for(int i = 0; i < startingRows; i++) {
            insertNewRow();
        }
    }

    // Creates a new row of blocks based on the current row we are working on
    public void insertNewRow() {
        this.currentRow++;
        for(int i = 0; i < this.maxColums; i++) {
            Block block = new Block(this.world, Block.blockWidth);
            block.setPosition(
                    i * block.getWidth() + (Block.blockWidth / 2) + 10,
                    BallShooter.HEIGHT - (Block.blockWidth / 2) + (currentRow * Block.blockWidth) - 1
            );
            block.setValue(10);

            this.stage.addActor(block);
        }
    }

    // Will create a new ball and place it into the world
    public void instantiateBall() {
        Ball b = new Ball(this.world);
        b.setPosition((BallShooter.WIDTH / 2), 0);
        b.setShootAngle(gameModel.getTouchAngle());
        b.launch();

        this.stage.addActor(b);
    }

    // UPDATE AND RENDER FOR THE LEVEL
    public void step(final float timestep, final int velIts, final int posIits) {
        this.world.step(timestep, velIts, posIits);
    }

    public void update() {
        if(this.shouldRestart) {
            this.restartLevel();
            gameModel.resetNewRowCounter();
            gameModel.resetNewRowCounter();

            return;
        }
        this.helper.setAngle(gameModel.getTouchAngle());

        destroyBodies();

        if(gameModel.instantiateNewBall()) {
            this.instantiateBall();

            gameModel.resetNewBallCounter();
        }

        if(gameModel.instantiateNewRow()) {
            this.insertNewRow();

            gameModel.resetNewRowCounter();
        }
    }

    // Will destroy any bodies that need to be destroyed
    private void destroyBodies() {
        if(!this.objectsToDestroy.isEmpty()) {
            for(int i = this.objectsToDestroy.size() - 1; i >= 0; i--) {
                BaseObject obj = this.objectsToDestroy.get(i);
                destroyBody(obj.getBody(), true);
            }
        }

        this.objectsToDestroy.removeAll(this.objectsToDestroy);
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
            this.shouldRestart = true;
//            if(!this.objectsToDestroy.contains(block)) {
//                this.objectsToDestroy.add(block);
//            }
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