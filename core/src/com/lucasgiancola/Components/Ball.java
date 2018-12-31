package com.lucasgiancola.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    public static float radius = 0.1f;
    private Vector2 velocity;
    private Vector2 position;

    private float moveSpeed = 20f;

    public boolean shouldDestroy = false;

    public Ball(float x, float y, float angle) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 1); // Create a vector straight up
        velocity.nor(); // Normalize it
        velocity.setAngle(angle); // Set the angle properly
        velocity.mulAdd(velocity, moveSpeed); // Set the proper speed in the right directing

        move();
    }

    public void draw(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);


        renderer.circle(position.x, position.y, Ball.radius * 100);
    }

    public void update(float width, float height) {
        move();

        if(position.x <= 0 || position.x >= width) {
            velocity.x *= -1;
        }

        if(position.y >= height) {
            velocity.y *= -1;
        }

        shouldDestroy = position.y <= 0;
    }

    private void move() {
        position.set(position.x + velocity.x, position.y + velocity.y);
    }
}
