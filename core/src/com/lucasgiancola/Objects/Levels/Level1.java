package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lucasgiancola.Objects.Balls.GameBall;
import com.lucasgiancola.Objects.Blocks.GameBlock;
import com.lucasgiancola.Objects.PhysicsObject;
import com.lucasgiancola.Objects.Triggers.Destroyer;

import java.util.ArrayList;

public class Level1 extends BaseLevel {
    private ArrayList<PhysicsObject> objects;
    private ArrayList<PhysicsObject> objectsToDestroy;

    private float currentTime = 0;

    public Level1(float levelWidth, float levelHeight) {
        super(levelWidth, levelHeight);

        objects = new ArrayList<PhysicsObject>();
        objects.add(new GameBlock(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset + 1000)));
        objectsToDestroy = new ArrayList<PhysicsObject>();
    }

    @Override
    public void update(float delta) {
        currentTime += delta;

        for(int i = objectsToDestroy.size() - 1; i >= 0; i--) {
            PhysicsObject toDestroy = objectsToDestroy.get(i);

            if(!toDestroy.isDestroyed) {
                levelWorld.destroyBody(toDestroy.body);
                toDestroy.isDestroyed = true;
            }



            if(toDestroy.isDestroyed && toDestroy.canDestroy) {
                objectsToDestroy.remove(toDestroy);
                objects.remove(toDestroy);
            }
        }

        if(currentTime >= 0.1f) {
            objects.add(new GameBall(levelWorld, new Vector2(xOffset + (worldWidth / 2), yOffset)));
            currentTime = 0;
        }

        for(PhysicsObject obj: objects) {
            obj.update(delta);
        }
    }

    @Override
    public void render() {
        // This top section is temporary
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect((-worldWidth / 2) + 1, (-worldHeight / 2) + 1, worldWidth - 1, worldHeight - 1);
        renderer.end();

        for(PhysicsObject obj: objects) {
            obj.render(renderer);
        }
    }

    private GameBlock getBlockFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof GameBlock) {
            return (GameBlock) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof GameBlock) {
            return (GameBlock) temp.getBody().getUserData();
        }

        return null;
    }

    private GameBall getBallFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof GameBall) {
            return (GameBall) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof GameBall) {
            return (GameBall) temp.getBody().getUserData();
        }

        return null;
    }

    private Destroyer getDestroyerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Destroyer) {
            return (Destroyer) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Destroyer) {
            return (Destroyer) temp.getBody().getUserData();
        }

        return null;
    }

    @Override
    public void beginContact(Contact contact) {
        GameBall ball = getBallFromContact(contact);
        GameBlock block = getBlockFromContact(contact);

        // If it's a ball hitting a block
        if(block != null && ball != null) {
            block.takeDamage(ball.damageAmount);

            if(block.health <= 0) {
                if(!this.objectsToDestroy.contains(block)) {
                    this.objectsToDestroy.add(block);
                }
            }
//
//            return;
        }

//        Destroyer destroyer = getDestroyerFromContact(contact);
//
//        if(destroyer != null && ball != null) {
//            if(!this.objectsToDestroy.contains(ball)) {
//                this.objectsToDestroy.add(ball);
//            }
//
//            return;
//        }
//
//        // Block hitting wall
//        if(destroyer != null && block != null) {
//            this.isOver = true;
//        }
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
