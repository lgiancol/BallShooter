package com.lucasgiancola.Screens;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Levels.GameLevel;
import com.lucasgiancola.Objects.Levels.LevelOne;

public class Screen implements com.badlogic.gdx.Screen {
    private GameLevel level;
    private Viewport viewport;

    public Screen() {
        viewport = new FitViewport(BallShooter.WIDTH, BallShooter.HEIGHT);
        level = new LevelOne(viewport);
    }

    @Override
    public void show() {
        viewport.setCamera(level.cam);
    }

    @Override
    public void render(float delta) {
        level.update(delta);
        level.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
