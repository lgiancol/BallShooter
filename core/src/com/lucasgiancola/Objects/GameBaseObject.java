package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public abstract class GameBaseObject {
    public Vector2 position;
    public Color color;

    public abstract void update(float delta);
    public abstract void render(ShapeRenderer renderer, Batch batch);
}
