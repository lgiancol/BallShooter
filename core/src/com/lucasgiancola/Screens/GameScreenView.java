package com.lucasgiancola.Screens;

import com.badlogic.gdx.Gdx;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Levels.BaseLevel;

public class GameScreenView extends BaseScreen {
    private BaseLevel level;
    private float dtAccumulator = 0;

    public GameScreenView(BallShooter app, BaseLevel level) {
        super(app);

        // Assign the level, and set up the width and height
        this.level = level;
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
            if ( true ) {
                level.step(FIXED_TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
                level.update(FIXED_TIMESTEP);
            }
        }

        // Clear the screen and render the level with the updated position
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT);;

        level.render();
    }
}
