package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.Balls.GameBall;
import com.lucasgiancola.Objects.Blocks.GameBlock;
import com.lucasgiancola.Objects.Triggers.BottomTrigger;
import com.lucasgiancola.Objects.Triggers.TopTrigger;
import com.lucasgiancola.Objects.Walls.Wall;

public class DefaultLevel extends Level {
    public DefaultLevel(Stage stage) {
        super(stage);

        instantiateBall();

        int wallThickness = 5;
        // Top
        stage.addActor(new Wall(world, new Vector2(stage.getWidth() / 2, stage.getHeight() - (wallThickness / 2)), stage.getWidth(), wallThickness));
        // Bottom
        stage.addActor(new Wall(world, new Vector2(stage.getWidth() / 2, (wallThickness / 2)), stage.getWidth(), wallThickness));

        // Left
        stage.addActor(new Wall(world, new Vector2((wallThickness / 2), stage.getHeight() / 2), wallThickness, stage.getHeight()));
        // Right
        stage.addActor(new Wall(world, new Vector2(stage.getWidth() - (wallThickness / 2) , stage.getHeight() / 2), wallThickness, stage.getHeight()));
    }

    public void instantiateBall() {
        Ball newBall = new Ball(world, new Vector2(stage.getWidth() / 2, 100));

        stage.addActor(newBall);
    }

    public void update(float delta) {
        counter += delta;

        if(counter >= 0.5f) {
            instantiateBall();
            counter = 0;
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

    private TopTrigger getTopTriggerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof TopTrigger) {
            return (TopTrigger) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof TopTrigger) {
            return (TopTrigger) temp.getBody().getUserData();
        }

        return null;
    }

    private BottomTrigger getBottomTriggerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof BottomTrigger) {
            return (BottomTrigger) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof BottomTrigger) {
            return (BottomTrigger) temp.getBody().getUserData();
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
//                blockCount++;
//                if(!objectsToDestroy.contains(block)) {
//                    objectsToDestroy.add(block);
//                }
            }

            return;
        }

        BottomTrigger destroyer = getBottomTriggerFromContact(contact);

        // Ball hit destroyer
        if(destroyer != null && ball != null) {
            // First time it hits the wall, it is added to the objects to destroy list, second time it actually gets removed
            // First time it hits the wall, it is added to the objects to destroy list, second time it actually gets removed
//            if(!objectsToDestroy.contains(ball)) {
//                objectsToDestroy.add(ball);
//            } else {
//                ball.canDestroyBody = true;
//                ball.canRemoveGraphic = true;
//            }

            return;
        }

        TopTrigger blockSpawner = getTopTriggerFromContact(contact);
        // Block hitting bottom
        if(blockSpawner != null && blockSpawner.active && block != null) {
            blockSpawner.active = false;
//            shouldSpawn = true;
        }


        // Block hitting bottom
        if(destroyer != null && block != null) {
            isOver = true;

//            if(!objectsToDestroy.contains(block)) {
//                block.canDestroyBody = true;
//                block.canRemoveGraphic = true;
//
//                objectsToDestroy.add(block);
//            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        TopTrigger blockSpawner = getTopTriggerFromContact(contact);

        if(blockSpawner != null) blockSpawner.active = true;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
