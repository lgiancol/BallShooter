package com.lucasgiancola;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.lucasgiancola.Screens.GameScreen;

public class Application extends Game {
    public static final int V_WIDTH = 1920, V_HEIGHT = 780;
    public float viewportWidth;
    public float viewportHeight;

    // These are the main variables that are going to be used throughout the app
    public static BitmapFont font;
    public OrthographicCamera camera;

    @Override
    public void create() {
        Application.font = new BitmapFont(Gdx.files.internal("data/font.fnt"));
        Application.font.setColor(Color.BLACK);
//        Application.font.getData().scale(1.5f);

        viewportWidth = Gdx.graphics.getWidth();
        viewportHeight = viewportWidth * 16 / 9;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);

        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        Application.font.dispose();
        getScreen().dispose();
    }
}
