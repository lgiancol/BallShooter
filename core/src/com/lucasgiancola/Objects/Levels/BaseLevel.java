package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lucasgiancola.Objects.PhysicsObject;

public abstract class BaseLevel implements ContactListener {
    protected ShapeRenderer renderer;
    protected Batch batch;
    protected float worldWidth = 0, worldHeight = 0;
    protected float xOffset = 0, yOffset = 0;
    protected World levelWorld;
    public Vector2 touch = new Vector2(0, 1);
    public boolean isOver = false;

    public BaseLevel(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        xOffset = -(this.worldWidth / 2);
        yOffset = -(this.worldHeight / 2);

        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        batch.enableBlending();
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        levelWorld = new World(new Vector2(0f, 0f), true);
        levelWorld.setContactListener(this);
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        renderer.setProjectionMatrix(projectionMatrix);
        batch.setProjectionMatrix(projectionMatrix);
    }

    public void step(final float timestep, final int velIts, final int posIts) {
        levelWorld.step(timestep, velIts, posIts);
    }

    public abstract void update(float delta);
    public abstract void render();

    public void dispose() {
        renderer.dispose();
        batch.dispose();

        Array<Body> bodies = new Array<Body>();
        levelWorld.getBodies(bodies);

        for(int i = bodies.size - 1; i >= 0; i--) {
            Body b = bodies.get(i);
            Object obj = b.getUserData();

            if(obj instanceof PhysicsObject) {
                ((PhysicsObject) obj).dispose();
            }
        }

        levelWorld.dispose();
    }

}
