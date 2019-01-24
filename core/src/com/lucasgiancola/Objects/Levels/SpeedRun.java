package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.Blocks.Block;
import com.lucasgiancola.Objects.GameObject;
import com.lucasgiancola.Objects.Walls.BlockSpawner;
import com.lucasgiancola.Objects.Walls.ObjectDespawner;
import com.lucasgiancola.Objects.Walls.Wall;

import java.util.ArrayList;

public class SpeedRun extends Level {

    private ArrayList<GameObject> destroyed = new ArrayList<GameObject>();
    private int numCols = 7;

    // Ball variables
    private float ballRadius = 0;

    // Block variables
    private float blockLength = 0;
    private float blockOffset = 20; // Distance between each block
    private Block topBlock = null;
    private boolean spawnRow = false;

    public SpeedRun(Stage stage) {
        super(stage);


        int wallThickness = 5;
        // Top
        stage.addActor(new BlockSpawner(world, new Vector2(stage.getWidth() / 2, stage.getHeight() - (wallThickness / 2)), stage.getWidth(), wallThickness));
        stage.addActor(new Wall(world, new Vector2(stage.getWidth() / 2, stage.getHeight() - (wallThickness / 2)), stage.getWidth(), wallThickness));
        // Bottom
        stage.addActor(new ObjectDespawner(world, new Vector2(stage.getWidth() / 2, (wallThickness / 2)), stage.getWidth(), wallThickness));

        // Left
        stage.addActor(new Wall(world, new Vector2((wallThickness / 2), stage.getHeight() / 2), wallThickness, stage.getHeight()));
        // Right
        stage.addActor(new Wall(world, new Vector2(stage.getWidth() - (wallThickness / 2) , stage.getHeight() / 2), wallThickness, stage.getHeight()));

        // Set the block and ball dimensions
        blockLength = ((stage.getWidth() - blockOffset) / numCols) - blockOffset;
        ballRadius = blockLength * 0.3f; // 15% the width of the block

        // Add the first row of blocks
        instantiateRow();

        // Instantiate the first ball
        instantiateBall();
    }

    public void instantiateBall() {
        Ball newBall = new Ball(world, new Vector2(stage.getWidth() / 2, 100), ballRadius);

        stage.addActor(newBall);
    }

    private void instantiateRow() {
        Block temp = null;

        for (int i = 0; i < numCols; i++) {
            if (MathUtils.randomBoolean(0.0f)) {
                float x = (i * blockLength + (blockLength / 2) + ((i + 1) * blockOffset));
                float y = stage.getHeight() + (blockLength / 2) + blockOffset;

                if (topBlock != null) {
                    // Top block's y + half the length of the block (since it is moved half) then a full blockLength then the blockOffset
                    y = y + blockLength - (blockOffset / 2);
                }
                temp = new Block(world, new Vector2(x, y), blockLength, blockLength);
                stage.addActor(temp);
            }
        }

        if (temp == null) {
            int col = MathUtils.random(0, numCols - 1);
            float x = (col * blockLength + (blockLength / 2) + ((col + 1) * blockOffset));
            float y = (stage.getHeight()+ (blockLength / 2) + blockOffset);

            if (topBlock != null) {
                y = y + blockLength - (blockOffset / 2);
            }

            temp = new Block(world, new Vector2(x, y), blockLength, blockLength);
            stage.addActor(temp);
        }

        topBlock = temp;
        spawnRow = false;
    }

    public void update(float delta) {
        counter += delta;

        if(counter >= 0.5f) {
            instantiateBall();
            counter = 0;
        }

        if(spawnRow) {
            instantiateRow();
        }

        if(!destroyed.isEmpty()) {
            for(GameObject go : destroyed) {
                // Disposes of the texture, removes it from the stage, and deletes it from the world
                go.dispose();
            }

            destroyed = new ArrayList<GameObject>();
        }
    }

    private Block getBlockFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Block) {
            return (Block) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Block) {
            return (Block) temp.getBody().getUserData();
        }

        return null;
    }

    private Ball getBallFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof Ball) {
            return (Ball) temp.getBody().getUserData();
        }

        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof Ball) {
            return (Ball) temp.getBody().getUserData();
        }

        return null;
    }

    private BlockSpawner getBlockSpawnerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof BlockSpawner) {
            return (BlockSpawner) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof BlockSpawner) {
            return (BlockSpawner) temp.getBody().getUserData();
        }

        return null;
    }

    private ObjectDespawner getDespawnerFromContact(Contact c) {
        Fixture temp = c.getFixtureA();

        if(temp != null && temp.getBody().getUserData() instanceof ObjectDespawner) {
            return (ObjectDespawner) temp.getBody().getUserData();
        }


        temp = c.getFixtureB();
        if(temp != null && temp.getBody().getUserData() instanceof ObjectDespawner) {
            return (ObjectDespawner) temp.getBody().getUserData();
        }

        return null;
    }

    @Override
    public void beginContact(Contact contact) {
        Ball ball = getBallFromContact(contact);
        Block block = getBlockFromContact(contact);

        // If it's a ball hitting a block
        if(block != null && ball != null) {
//            block.takeDamage(ball.damageAmount);

//            if(block.health <= 0) {
//                blockCount++;
//                if(!objectsToDestroy.contains(block)) {
//                    objectsToDestroy.add(block);
//                }
//            }

            return;
        }

        ObjectDespawner despawner = getDespawnerFromContact(contact);

        // Ball hit destroyer
        if(despawner != null && ball != null) {
            // First time it hits the wall, it is added to the objects to destroy list, second time it actually gets removed
            if(!destroyed.contains(ball)) {
                destroyed.add(ball);
            }
//            if(!objectsToDestroy.contains(ball)) {
//                objectsToDestroy.add(ball);
//            } else {
//                ball.canDestroyBody = true;
//                ball.canRemoveGraphic = true;
//            }

            return;
        }

        BlockSpawner blockSpawner = getBlockSpawnerFromContact(contact);

        // Block hitting bottom
        if(blockSpawner != null && blockSpawner.isActive && block != null) {
            blockSpawner.isActive = false;
            spawnRow = true;
        }


        // Block hitting bottom
        if(despawner != null && block != null) {
//            isOver = true;

            if(!destroyed.contains(block)) {
                destroyed.add(block);
            }

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
        BlockSpawner blockSpawner = getBlockSpawnerFromContact(contact);

        if(blockSpawner != null) blockSpawner.isActive = true;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
