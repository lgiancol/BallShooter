package com.lucasgiancola.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.lucasgiancola.Interfaces.Drawable;

public class Block implements Drawable {
    private float length;
    private float fallSpeed = -100f;
    public static float spacing = 30f;

    public Body body;

    public Block(Body body, float length) {
        this.body = body;
        this.length = length - Block.spacing * 2;

//        fall();
    }

    public void draw(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.rect(body.getPosition().x, body.getPosition().y, length, length);
    }

    public void update() {
        fall();
    }

    private void fall() {
        body.setLinearVelocity(0, fallSpeed);
//        body.applyForceToCenter(new Vector2(0, fallSpeed), true);
    }

}
