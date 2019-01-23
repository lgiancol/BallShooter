package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Level implements ContactListener {
    protected Stage stage;
    protected World world;

    protected float counter = 0;
    public boolean isOver = false;

    public Level(Stage stage) {
        this.stage = stage;

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(this);
    }

    public abstract void update(float delta);

    public void step(final float timestep, final int velIts, final int posIts) {
        world.step(timestep, velIts, posIts);
    }


}
