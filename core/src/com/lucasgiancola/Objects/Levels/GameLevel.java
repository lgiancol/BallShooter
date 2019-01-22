package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameLevel extends Stage implements ContactListener {

    public Camera cam;
    protected World world;

    public GameLevel(Viewport dimensions) {
        cam = new OrthographicCamera(dimensions.getWorldWidth(), dimensions.getWorldHeight());

        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(this);
    }

    public void update(float delta) {
        this.act(delta);

        cam.update();
    }
}
