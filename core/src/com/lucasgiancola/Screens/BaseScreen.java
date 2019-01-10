package com.lucasgiancola.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lucasgiancola.BallShooter;

public class BaseScreen implements Screen {
    protected final Stage graphicsLayer;
    protected final Stage uiLayer;
    protected final BallShooter ballShooter;

    public BaseScreen(BallShooter ballShooter) {
        this.ballShooter = ballShooter;
        this.graphicsLayer = new Stage(new FitViewport(BallShooter.WIDTH, BallShooter.HEIGHT)) {
            @Override
            public boolean keyDown(int keyCode) {
                if(keyCode == Input.Keys.BACK || keyCode == Input.Keys.ESCAPE) {
                    if(backClicked()) {
                        return true;
                    } else {
                        Gdx.app.exit();
                    }
                }

                return true;
            }
        };

        this.uiLayer = new Stage(this.graphicsLayer.getViewport());

        InputMultiplexer inputHandler = new InputMultiplexer();
        inputHandler.addProcessor(this.graphicsLayer);
        inputHandler.addProcessor(this.uiLayer);

        Gdx.input.setInputProcessor(inputHandler);
        Gdx.input.setCatchBackKey(true);
    }

    private boolean backClicked() {
        return false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        graphicsLayer.act(delta);
        graphicsLayer.draw();

        uiLayer.act(delta);
        uiLayer.draw();
    }

    @Override
    public void show() {
        InputMultiplexer inputHandler = new InputMultiplexer();
        inputHandler.addProcessor(this.graphicsLayer);
        inputHandler.addProcessor(this.uiLayer);

        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void resize(int w, int h) {
        this.graphicsLayer.getViewport().update(w, h);
        this.uiLayer.getViewport().update(w, h);
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        this.graphicsLayer.dispose();
        this.uiLayer.dispose();
    }
}
