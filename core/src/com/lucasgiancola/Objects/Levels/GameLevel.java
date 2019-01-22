package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameLevel extends Stage implements ContactListener {

    public Camera cam = null;
    protected World world;

    public GameLevel() {
        world = new World(new Vector2(0f, 0f), true);
        world.setContactListener(this);
    }

    public void setViewport(Viewport vp) {
        cam = new OrthographicCamera(vp.getWorldWidth(), vp.getWorldHeight());
        vp.setCamera(cam);

        System.out.println("WW: " + vp.getWorldWidth() + " WH: " + vp.getWorldHeight());

        cam.update();
    }

    public void update(float delta) {
        if(cam == null) throw new IllegalArgumentException("Camera cannot be null");

        this.act(delta);
    }
}
