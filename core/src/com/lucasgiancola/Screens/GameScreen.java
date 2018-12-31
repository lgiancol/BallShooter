package com.lucasgiancola.Screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucasgiancola.Actors.Rectangle;
import com.lucasgiancola.Components.Ball;
import com.lucasgiancola.Components.Block;
import com.lucasgiancola.Components.Level;
import com.lucasgiancola.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.lucasgiancola.Input;

public class GameScreen implements Screen {
    final Game game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private float width = 1080, height = 1080;
    private Rectangle debugRect;

    private Level level;
    private ShapeRenderer renderer;

    public GameScreen(Game game) {
        this.game = game;

        camera = new OrthographicCamera();

        // Create a 9x16 aspect ratio
        width = Gdx.graphics.getWidth();
        height = width * 16 / 9;

        viewport = new FitViewport(width, height, camera);
        viewport.apply();

        camera.position.set(0, 0, 0);
        camera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);

        debugRect = new Rectangle(0, 0, camera.viewportWidth, camera.viewportHeight, 10);
        debugRect.setDebug(true);
        debugRect.setColour(null);

        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);

        level = new Level(camera.viewportWidth, camera.viewportHeight);

        Game.input.setCam(camera);
        Game.input.setVp(viewport);
        Gdx.input.setInputProcessor(Game.input);
    }

    @Override
    public void render(float delta) {
        camera.update();
        // Calculates the proper touch positions... I don't like doing it this way!
//        if(Gdx.input.isTouched()) {
//            System.out.println("Touched in GameScreen");
//            Game.input.setViewportTouchCoords(camera, viewport);
//        }

        Gdx.gl.glClearColor(1.0f, 0, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin();
        this.game.batch.begin();

        level.draw(renderer, game.batch, game.font);

        this.game.batch.end();
        renderer.end();

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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
    }
}