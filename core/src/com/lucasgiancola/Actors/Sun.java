package com.lucasgiancola.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lucasgiancola.Managers.Assets;

public class Sun extends Image {
    private Texture img;
    private float x = 0, y = 0;
    private Vector3 touchPos = new Vector3();

    private Vector3 position = new Vector3(0, 0, 0);
    private Vector3 velocity = new Vector3(0, 0, 0);
    private Vector3 acceleration = new Vector3(0, 0, 0);

    public Sun() {

        x = this.getWidth() / 2;
        y = this.getHeight() / 2;

        this.setPosition(x, y);
    }

    public void followTouch(Camera cam) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(touchPos);

        this.moveBy(touchPos.x - x, touchPos.y - y);
        x = touchPos.x;
        y = touchPos.y;
    }
}
