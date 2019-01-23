package com.lucasgiancola.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lucasgiancola.Application;
import com.lucasgiancola.Objects.Balls.Ball;

public abstract class Screen implements com.badlogic.gdx.Screen {
    protected final Application app;
    protected Stage stage;

    public Screen(Application app) {
        this.app = app;

        stage = new Stage(new FitViewport(app.viewportWidth, app.viewportHeight, app.camera));
        Gdx.input.setInputProcessor(stage);

        stage.addActor(new Ball());
    }
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        app.batch.setProjectionMatrix(app.camera.combined);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
