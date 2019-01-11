package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class GameBaseObject {
    public Vector2 position;
    public float width, height;
    public Body body;

    public abstract void update(float delta);
    public abstract void render(ShapeRenderer renderer);
}
