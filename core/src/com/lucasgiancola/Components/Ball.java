package com.lucasgiancola.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Ball {
    public static float radius = 25f;
    private Vector2 velocity;
    private Vector2 position;

    private float moveSpeed = 40f;

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

        renderer.circle(position.x, position.y, Ball.radius);
    }

    public void update(float width, float height) {
        move();

        if(position.x <= 0) {
            position.x = Ball.radius;
            velocity.x *= -1;
        } else if(position.x >= width) {
            position.x = width - Ball.radius;
            velocity.x *= -1;
        }

        if(position.y >= height) {
            position.y = height - Ball.radius;
            velocity.y *= -1;
        }

        shouldDestroy = position.y <= 0;
    }

    private void move() {
        position.set(position.x + velocity.x, position.y + velocity.y);
    }

    public void checkCollisions(ArrayList<Block> blocks, int rows, int cols) {

//        for(int r = 0; r < rows; r++) {
//            for(int c = 0; c < cols; c++) {
//
//            }
//        }

        int count = 0;
        for(int i = blocks.size() - 1; i >= 0; i--) {
            Block b = blocks.get(i);

            if(b.isVisible) {
                if(b.didHitBottom(this)) {
                    this.position.y = b.getY() - Ball.radius;
                    this.velocity.y *= -1;

                    if(b.hit()) blocks.remove(b);
                }

                else if(b.didHitTop(this)) {
                    this.position.y = (b.getY() + b.getWidth()) + Ball.radius;
                    this.velocity.y *= -1;

                    if(b.hit()) blocks.remove(b);
                }
                else if(b.didHitLeft(this)) {
                    this.position.x = b.getX() - Ball.radius;
                    this.velocity.x *= -1;

                    if(b.hit()) blocks.remove(b);
                }
                else if(b.didHitRight(this)) {
                    this.position.x = (b.getX() + b.getWidth()) + Ball.radius;
                    this.velocity.x *= -1;

                    if(b.hit()) blocks.remove(b);
                }

                count++;
            }
        }

    }

    public Vector2 getPosition() {
        return position;
    }

    public float getTop() {
        return this.position.y + Ball.radius;
    }
    public float getBottom() {
        return this.position.y - Ball.radius;
    }
    public float getLeft() {
        return this.position.x - Ball.radius;
    }
    public float getRight() {
        return this.position.x + Ball.radius;
    }
}
