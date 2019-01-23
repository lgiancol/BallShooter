package com.lucasgiancola;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lucasgiancola.Screens.SplashScreen;

public class Application extends Game {
    public static final int V_WIDTH = 1920, V_HEIGHT = 780;
    public float viewportWidth;
    public float viewportHeight;

    public OrthographicCamera camera;
    public SpriteBatch batch;

    @Override
    public void create() {
        viewportWidth = Gdx.graphics.getWidth();
        viewportHeight = viewportWidth * 16 / 9;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        batch = new SpriteBatch();

        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        batch.dispose();
    }
}
