package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class BaseLevel {
    protected ShapeRenderer renderer;
    protected float worldWidth = 0, worldHeight = 0;
    protected float xOffset = 0, yOffset = 0;
    protected World levelWorld;

    public BaseLevel(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        xOffset = -(this.worldWidth / 2);
        yOffset = -(this.worldHeight / 2);

        renderer = new ShapeRenderer();

        levelWorld = new World(new Vector2(0f, 0f), true);
    }

    public void setProjectionMatrix(Matrix4 projectionMatrix) {
        renderer.setProjectionMatrix(projectionMatrix);
    }

    public void step(final float timestep, final int velIts, final int posIts) {
        levelWorld.step(timestep, velIts, posIts);
    }

    public abstract void update(float delta);
    public abstract void render();

}
