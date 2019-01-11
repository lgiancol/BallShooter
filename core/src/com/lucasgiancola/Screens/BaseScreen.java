package com.lucasgiancola.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucasgiancola.BallShooter;

public abstract class BaseScreen implements Screen {
    protected BallShooter app;

    protected Camera mainCamera;
    protected Viewport viewport;

    public BaseScreen(BallShooter app) {
        this.app = app;

        // Creates a viewport area that will be the dimensions of the playable area
        viewport = new FitViewport(BallShooter.WIDTH, BallShooter.HEIGHT);
        mainCamera = new OrthographicCamera(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public abstract void render(float delta);

    @Override
    public void resize(int width, int height) {
        // Updates the viewport width and height, and then creates a new camera to use them
        viewport.update(width, height);

        mainCamera = new OrthographicCamera(viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    @Override
    public void dispose() {

    }
}
