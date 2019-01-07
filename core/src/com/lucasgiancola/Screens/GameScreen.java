package com.lucasgiancola.Screens;

import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Models.GameModel;
import com.lucasgiancola.Objects.Levels.SpeedRun;
import com.lucasgiancola.Objects.Levels.Level;

public class GameScreen extends AbstractScreen {
    private GameModel gameModel;
    private float dtAccumulator = 0f;

    private Level level;

    public GameScreen(BallShooter ballShooter) {
        super(ballShooter);

        this.gameModel = new GameModel();
        this.gameModel.setStage(this.stage);
        this.level = new SpeedRun(this.stage, this.gameModel);
    }

    @Override
    public void show() {
        super.show();

        level.init();
    }

    private void updateGame(float delta) {
        gameModel.update(delta);

        level.update(delta);
    }

    @Override
    public void render(float delta) {

        // Call box2d with a fixed timestep
        final int VELOCITY_ITERATIONS = 15;
        final int POSITION_ITERATIONS = 3;
        final float FIXED_TIMESTEP = 1.0f / 60.0f;
        this.dtAccumulator += delta;

        while (this.dtAccumulator > FIXED_TIMESTEP) {
            this.dtAccumulator -= FIXED_TIMESTEP;
            if ( this.gameModel.isPlaying() ) {
                level.step(FIXED_TIMESTEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
                updateGame(FIXED_TIMESTEP);
            }
        }

        super.render(delta);

    }
}