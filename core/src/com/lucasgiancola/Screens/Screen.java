package com.lucasgiancola.Screens;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Levels.GameLevel;

public class Screen implements com.badlogic.gdx.Screen {
    private GameLevel level;
    protected Viewport viewport;

    public Screen(GameLevel stage) {
        viewport = new FitViewport(BallShooter.WIDTH, BallShooter.HEIGHT);
        level = stage;
    }

    @Override
    public void show() {
        level.setViewport(viewport);
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
