package com.lucasgiancola;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Input implements InputProcessor {
    private Camera cam;
    private Viewport vp;
    public Vector3 viewportTouchCoords = new Vector3(0, 0, 0);

    public Input() {

    }

    public Input(Camera cam, Viewport viewport) {
        this.cam = cam;
        this.vp = viewport;
    }

    public void setCam(Camera cam) {
        this.cam = cam;
    }

    public void setVp(Viewport vp) {
        this.vp = vp;
    }

    public void setViewportTouchCoords() {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);

        viewportTouchCoords = cam.unproject(touchPos, vp.getScreenX(), vp.getScreenY(), vp.getScreenWidth(), vp.getScreenHeight());
    }

    public boolean keyDown (int keycode) {
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        System.out.println("Cheeeeeeee");
        setViewportTouchCoords();

        return true;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        System.out.println("Cheeeeeeee");
        return true;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        System.out.println("Cheeeeeeee");
        setViewportTouchCoords();

        return true;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
}
