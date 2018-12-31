package com.lucasgiancola.Components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Block {
    private Vector2 velocity;
    private Vector2 position;
    private float width;

    private float moveSpeed = 0f;
    private int value;

    public boolean isVisible = true;

    public Block(float x, float y, float width, int value) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0); // Create a vector straight up
        velocity.nor(); // Normalize it
        velocity.mulAdd(velocity, moveSpeed); // Set the proper speed in the right directing

        this.width = width;

        this.value = value;
    }

    public void draw(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        renderer.rect(position.x, position.y, width, width);

        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(position.x, position.y, width, width);

        renderer.circle(position.x, position.y, 10);
    }

    public void update() {
        move();
    }

    private void move() {
        position.set(position.x + velocity.x, position.y + velocity.y);
    }

    public Vector2 getCenter() {
        return new Vector2(position.x + (width / 2), position.y + (width / 2));
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public boolean hitTop(Ball b) {
        return isVisible &&
                b.getBottom() >= this.position.y + (this.width / 2) && // Bottom of ball >= Middle of block
                b.getBottom() <= this.position.y + this.width && // Bottom of ball <= top of block
                b.getLeft() <= this.position.x + this.width && // Left of ball <= right of block
                b.getRight() >= this.position.x; // Right of ball >= left of block
    }

    public boolean hitBottom(Ball b) {
        return isVisible &&
                b.getTop() <= this.position.y + (this.width / 2) && // Bottom of ball <= Middle of block
                b.getTop() >= this.position.y && // Top of ball >= bottom of block
                b.getLeft() <= this.position.x + this.width && // Left of ball <= right of block
                b.getRight() >= this.position.x; // Right of ball >= left of block
    }

    public boolean hitLeft(Ball b) {
        return isVisible &&
                b.getRight() >= this.position.x &&
                b.getRight() <= this.position.x + this.width &&
                b.getTop() >= this.position.y &&
                b.getBottom() <= this.position.y + this.width;
    }

    public boolean hitRight(Ball b) {
        return isVisible &&
                b.getLeft() >= this.position.y + (this.width / 2) && // Bottom of ball >= Middle of block
                b.getLeft() <= this.position.y + this.width && // Bottom of ball <= top of block
                b.getTop() >= this.position.y &&
                b.getBottom() <= this.position.y + this.width;
    }
}
