package com.lucasgiancola.Screens;

import com.lucasgiancola.Application;
import com.lucasgiancola.Objects.Levels.Level;

public class NewGameScreen extends Screen {

    private Level level;

    // Temp
    float dtAccumulator = 0;

    public NewGameScreen(Application app) {
        super(app);

        level = new Level(stage);
    }

    @Override
    public void update(float delta) {
        // Check level variables to see what we should do with the level/screen we are on
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
//                level.update(FIXED_TIMESTEP);
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        super.render(delta);
    }

    @Override
    public void show() {
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
}
