package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lucasgiancola.Objects.Balls.GameBall;
import com.lucasgiancola.Objects.Blocks.GameBlock;

public class Level1 extends BaseLevel {
    private GameBlock block;
    private GameBall ball;

    public Level1(float levelWidth, float levelHeight) {
        super(levelWidth, levelHeight);

        block = new GameBlock(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset + 400));

        ball = new GameBall(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset));
    }

    @Override
    public void update(float delta) {
        block.update(delta);
        ball.update(delta);
    }

    @Override
    public void render() {
        // This top section is temporary
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect((-worldWidth / 2) + 1, (-worldHeight / 2) + 1, worldWidth - 1, worldHeight - 1);
        renderer.end();

        // Render all the objects
        block.render(renderer);
        ball.render(renderer);
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println("Contact!!!");
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
