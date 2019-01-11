package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.Balls.GameBall;
import com.lucasgiancola.Objects.Blocks.GameBlock;
import com.lucasgiancola.Objects.PhysicsObject;
import com.lucasgiancola.Objects.Triggers.BottomTrigger;
import com.lucasgiancola.Objects.Triggers.TopTrigger;
import com.lucasgiancola.Objects.Triggers.Wall;

import java.util.ArrayList;

public class Level1 extends BaseLevel {
    private ArrayList<PhysicsObject> objects;
    private ArrayList<PhysicsObject> objectsToDestroy;

    // Blocks variables
    private int numCols = 7;
    private float blockWidth = 0;
    private float blockOffset = 25;
    private GameBlock topBlock = null;
    private boolean shouldSpawn = false;

    private float currentTime = 0;

    public Level1(float levelWidth, float levelHeight) {
        super(levelWidth, levelHeight);

        blockWidth = ((levelWidth - blockOffset) / numCols) - blockOffset;

        objects = new ArrayList<PhysicsObject>();
//        objects.add(new GameBlock(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset + 1000)));
        objectsToDestroy = new ArrayList<PhysicsObject>();

        // Bottom
        PhysicsObject wall = new BottomTrigger(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset), levelWidth);
        objects.add(wall);

        // Left
        wall = new Wall(levelWorld, new Vector2(xOffset, yOffset + (levelHeight / 2)), 2, levelHeight);
        objects.add(wall);

        // Right
        wall = new Wall(levelWorld, new Vector2(xOffset + levelWidth, yOffset + (levelHeight / 2)), 2, levelHeight);
        objects.add(wall);

        // Top
        wall = new TopTrigger(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset + levelHeight), levelWidth);
        objects.add(wall);

        wall = new Wall(levelWorld, new Vector2(xOffset + (levelWidth / 2), yOffset + levelHeight), levelWidth, 2);
        objects.add(wall);


        insertRow();
    }

    private void insertRow() {
            GameBlock temp = null;
            for (int i = 0; i < numCols; i++) {
                if (MathUtils.randomBoolean(0.5f)) {
                    float x = xOffset + (i * blockWidth + (blockWidth / 2) + ((i + 1) * blockOffset));
                    float y = yOffset + (worldHeight + (blockWidth / 2) + blockOffset);

                    if (topBlock != null) {
                        y = (Constants.toScreenUnits(topBlock.body.getPosition().y) + blockWidth + blockOffset);
                    }
                    temp = new GameBlock(levelWorld, new Vector2(x, y), blockWidth);

                    objects.add(temp);
                }
            }

            if (temp == null) {
//            System.out.println("Didn't create it in the loop so creating one outside loop");
                int col = MathUtils.random(0, numCols - 1);
                float x = xOffset + (col * blockWidth + (blockWidth / 2) + ((col + 1) * blockOffset));
                float y = yOffset + (worldHeight + (blockWidth / 2) + blockOffset);

                if (topBlock != null) {
                    y = (Constants.toScreenUnits(topBlock.body.getPosition().y) + blockWidth + blockOffset);
                }
                temp = new GameBlock(levelWorld, new Vector2(x, y), blockWidth);

                objects.add(temp);
            }

            topBlock = temp;
            shouldSpawn = false;
    }

    /**
     * Will destroy physics bodies from the world and will also remove them from rendering once ready
     */
    private void handleObjects() {
        if(shouldSpawn) {
            insertRow();
        }

        // Go through list of objects that should be destroyed and destroy them from the world
        //  then once ready, remove them from the scene
        for(int i = objectsToDestroy.size() - 1; i >= 0; i--) {
            PhysicsObject toDestroy = objectsToDestroy.get(i);

            if(!toDestroy.isBodyDestroyed && toDestroy.canDestroyBody) {
                // Check what kind of physics object it was and add any power ups needed
                levelWorld.destroyBody(toDestroy.body);
                toDestroy.isBodyDestroyed = true;
            }

            if(toDestroy.canRemoveGraphic) {
                // Remove from proper lists
                objects.remove(toDestroy);
                objectsToDestroy.remove(toDestroy);
            }
        }
    }

    @Override
    public void update(float delta) {
        currentTime += delta;

        handleObjects();

        if(currentTime >= 0.1f) {
            objects.add(new GameBall(levelWorld, new Vector2(xOffset + (worldWidth / 2), yOffset)));
            currentTime = 0;
        }


//        System.out.println("Block Top Edge: " + Constants.toScreenUnits(topBlock.body.getPosition().y) + (blockWidth / 2) + "Trigger: " + (yOffset + worldHeight));
//        if(Constants.toScreenUnits(topBlock.body.getPosition().y) + (blockWidth / 2) <= (yOffset + worldHeight)) {
//            insertRow();
//        }

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
            obj.render(renderer, batch);
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
                if(!objectsToDestroy.contains(block)) {
                    objectsToDestroy.add(block);
                }
            }

            return;
        }

        BottomTrigger destroyer = getBottomTriggerFromContact(contact);

        // Ball hit destroyer
        if(destroyer != null && ball != null) {
            // First time it hits the wall, it is added to the objects to destroy list, second time it actually gets removed
            // First time it hits the wall, it is added to the objects to destroy list, second time it actually gets removed
            if(!objectsToDestroy.contains(ball)) {
                objectsToDestroy.add(ball);
            } else {
                ball.canDestroyBody = true;
                ball.canRemoveGraphic = true;
            }

            return;
        }

        TopTrigger blockSpawner = getTopTriggerFromContact(contact);
        // Block hitting bottom
        if(blockSpawner != null && blockSpawner.active && block != null) {
            blockSpawner.active = false;
            shouldSpawn = true;
        }


        // Block hitting bottom
        if(destroyer != null && block != null) {
            if(!objectsToDestroy.contains(block)) {
                block.canDestroyBody = true;
                block.canRemoveGraphic = true;

                objectsToDestroy.add(block);
            }
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
