package com.lucasgiancola.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.Game;

import java.util.ArrayList;

public class Level {
    private ArrayList<Block> blocks;
    private ArrayList<Ball> balls;

    private int cols = 8;
    private int rows = 3;
    private float width, height;

    private float newBallSpawn = 0.1f; // Number of milliseconds before a new ball spawns
    private float currentTime = 0;
    private int maxBalls = 10;
    private boolean reset = true;

    private Vector2 currentTouch;
    private Vector2 pivot;

    public Level(float width, float height) {
        this.width = width;
        this.height = height;

        currentTouch = new Vector2(width / 2, height);
        pivot = new Vector2(width / 2, 10);

        resetLevel();

        balls = new ArrayList<Ball>();
        balls.add(new Ball(width / 2, Ball.radius, (float) calculateAngle(pivot, currentTouch)));
    }

    private void resetLevel() {
        reset = true;
        initBlocks();
    }

    private void initBlocks() {
        blocks = new ArrayList<Block>();

        float blockWidth = (width / cols);

        for(int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                float x = c * blockWidth;
                float y = height - ((rows - r - 1) * blockWidth) - blockWidth;

                blocks.add(new Block(x, y, blockWidth, 6));
            }
        }
    }

    public void draw(ShapeRenderer renderer, SpriteBatch batch, BitmapFont font) {
        reset = true;

        if(shouldMakeNewBall()) {
            balls.add(new Ball(width / 2, Ball.radius, (float) calculateAngle(pivot, currentTouch)));
        }

        // Set the current touch location
        if(Gdx.input.isTouched()) {
            System.out.println("Touched in Level");
            currentTouch = new Vector2(Game.input.viewportTouchCoords.x, Game.input.viewportTouchCoords.y);
        }

        renderBackground(renderer);

        renderLine(pivot, currentTouch, renderer);

        // Update and draw all the balls
        for(int i = balls.size() - 1; i >= 0; i--) {
            Ball b = balls.get(i);

            b.update(width, height);

            if(b.shouldDestroy) {
                balls.remove(b);
            } else {
                b.draw(renderer);

                b.checkCollisions(blocks, rows, cols);
            }
        }

        for(Block b : blocks) {
            b.update();
            if(b.isVisible) {
                b.draw(renderer, batch, font);
                reset = false;
            }
        }

        if(reset) resetLevel();
    }

    private boolean shouldMakeNewBall() {
        currentTime += Gdx.graphics.getDeltaTime();

        if(currentTime >= newBallSpawn) {
            currentTime = 0;

//            return balls.size() < maxBalls;
            return true;
        }

        return false;
    }

    private double calculateAngle(Vector2 start, Vector2 end) {
        return Math.atan2(end.y - start.y, end.x - start.x) * 180.0 / Math.PI;
    }

    private void renderPoint(Vector2 point, ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Line);
//        renderer.setColor(Color.GREEN);
        renderer.circle(point.x, point.y, 50f);
    }

    private void renderLine(Vector2 point1, Vector2 point2, ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.GREEN);
        renderer.line(point1, point2);
    }

    private void renderBackground(ShapeRenderer renderer) {
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, width, height);
    }
}
