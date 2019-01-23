package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.Blocks.Block;

public class Level {
    private Stage stage;
    public World world;

    public boolean isOver = false;

    public Level(Stage stage) {
        this.stage = stage;

        world = new World(new Vector2(0, 0), true);
        // Set this class to be a contact listener and set it to be the world's contact listener

        instantiateBall();

        stage.addActor(new Block(world, new Vector2(stage.getWidth() / 2, stage.getHeight())));
        stage.addActor(new Block(world, new Vector2(stage.getWidth() / 2, 0)));
    }

    public void instantiateBall() {
        Ball newBall = new Ball(world, new Vector2(stage.getWidth() / 2, 300));

        stage.addActor(newBall);
    }

    public void step(final float timestep, final int velIts, final int posIts) {
        world.step(timestep, velIts, posIts);
    }


}
