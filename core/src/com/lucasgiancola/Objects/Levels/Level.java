package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.lucasgiancola.Models.GameModel;
import com.lucasgiancola.Objects.BaseObject;

public abstract class Level implements ContactListener {
    // Level variables
    GameModel gameModel;
    Stage stage;

    // Physics variables
    World world = null;

    public Level(Stage stage, GameModel gameModel) {
        this.gameModel = gameModel;
        this.stage = stage;

        // Initialize the physics world
        this.initWorld();
    }

    /**
     * All physics worlds for any level will be created in the same way, so this can be created right here in the Level class
     */
    private void initWorld() {
        Vector2 gravity = new Vector2(0 ,0); // No gravity

        world = new World(gravity, true);
        World.setVelocityThreshold(0f);
        world.setContactListener(this);
    }

    /**
     * Will remove all physics objects from the level, as well as remove any references that they may have inside of them
     */
    protected void resetWorld() {
        // If for some reason the world is not created yet, create it and return
        if(this.world == null) {
            this.initWorld();
            return;
        }

        // Store all bodies in an array
        Array<Body> bodies = new Array<Body>();
        this.world.getBodies(bodies);

        for(Body b: bodies) {
            this.removeBody(b);
        }
    }

    /**
     * Will remove a physics body from the world. Will first dispose of all inner variables then remove it from the mainStage and the world
     * @param toRemove The body to remove of from the game
     */
    protected void removeBody(Body toRemove) {
        Object userData = toRemove.getUserData();

        if(userData instanceof BaseObject) {
            // Sets the userData to null and removes itself from the mainStage
            ((BaseObject) userData).dispose();
            // Removes the body from the world
            this.world.destroyBody(toRemove);
        }
    }

    /**
     * The main method for a level that will initialize all aspects of the level
     */
    public abstract void init();

    /**
     * Will created whatever boundaries the current level has. Doesn't necessarily have to be walls
     */
    abstract void setupBoundaries();

    /**
     * A way to restart a level back to it's initial state
     */
    abstract void restart();

    /**
     * Will create a new ball and send it on it's way
     */
    abstract void shootBall();

    public abstract void step(final float step, final int velIts, final int posIts);

    /**
     * This will get called every frame so the actual values of the levels can be checked and modified
     */
    public abstract void update(float delta);
}
