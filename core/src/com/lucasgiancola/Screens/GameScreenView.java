package com.lucasgiancola.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Levels.BaseLevel;
import com.lucasgiancola.Objects.Levels.Level1;

public class GameScreenView extends BaseScreen implements InputProcessor {
    private BaseLevel level;
    private float dtAccumulator = 0;

    public GameScreenView(BallShooter app, BaseLevel level) {
        super(app);

        // Assign the level, and set up the width and height
        this.level = level;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        level.setProjectionMatrix(mainCamera.combined);

        // Call box2d with a fixed timestep
        final int VELOCITY_ITERATIONS = 15;
        final int POSITION_ITERATIONS = 3;
        final float FIXED_TIMESTEP = 1.0f / 60.0f;
        dtAccumulator += delta;

        while (dtAccumulator > FIXED_TIMESTEP) {
            dtAccumulator -= FIXED_TIMESTEP;
            // Should check if the game is running/isn't paused
            if ( !level.isOver ) {
                level.step(FIXED_TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
                level.update(FIXED_TIMESTEP);
            } else {
                app.setScreen(new GameScreenView(app, new Level1(BallShooter.WIDTH, BallShooter.HEIGHT)));
                this.dispose();
                return;
            }
        }

        // Clear the screen and render the level with the updated position
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        level.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        level.dispose();
    }

    /* Input processing */

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        level.touch = viewport.unproject(new Vector2(screenX, screenY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        level.touch = viewport.unproject(new Vector2(screenX, screenY));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
