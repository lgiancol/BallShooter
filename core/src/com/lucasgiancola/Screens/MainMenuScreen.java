package com.lucasgiancola.Screens;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucasgiancola.BallShooter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.lucasgiancola.Managers.Assets;

public class MainMenuScreen implements Screen {
    final BallShooter ballShooter;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;

    public MainMenuScreen(BallShooter game) {
        this.ballShooter = game;

        Table menuTable = new Table();
        menuTable.setSize(stage.getWidth(), stage.getHeight());
        menuTable.setFillParent(true);
        menuTable.top();


        Image title = new Image(atlas.createSprite("title"));
        title.setPosition(menuTable.getWidth() / 2 - title.getWidth() / 2, menuTable.getHeight() - 100);

        menuTable.addActor(title);


        Gdx.input.setInputProcessor(stage);

        stage.addActor(menuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 0, 1.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isTouched()) {
            this.ballShooter.setScreen(new GameScreen(this.ballShooter));
            this.dispose();
        }

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
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
    }
}
