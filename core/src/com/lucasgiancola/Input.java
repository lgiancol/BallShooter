package com.lucasgiancola;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Input {
    public Vector3 viewportTouchCoords;

    public void setViewportTouchCoords(Camera cam, Viewport vp) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        viewportTouchCoords = cam.unproject(touchPos, vp.getScreenX(), vp.getScreenY(), vp.getScreenWidth(), vp.getScreenHeight());
    }
}
