package com.lucasgiancola;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucasgiancola.Managers.Assets;
import com.lucasgiancola.Managers.SoundManager;
import com.lucasgiancola.Objects.Levels.SpeedRunner;
import com.lucasgiancola.Screens.GameScreen;
import com.lucasgiancola.Screens.NewScreen;

public class BallShooter extends Game {
	public static float WIDTH = 9;
	public static float HEIGHT = 16;

	public static BitmapFont font;
	public static ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		Assets.getInstance().load(); // Will load all necessary assets for the game
		SoundManager.instance.init();
		updateDimensions();

		BallShooter.font = new BitmapFont(Gdx.files.internal("data/casual.fnt"));
		BallShooter.shapeRenderer = new ShapeRenderer();

		// The level is hard coded here, but in the real game, once the user clicks on a level/starts a new chaos mode it would start that level instead
//		setScreen(new GameScreen(this, new SpeedRunner(WIDTH, HEIGHT)));
		setScreen(new NewScreen());
	}

	private void updateDimensions() {
		BallShooter.WIDTH = Gdx.graphics.getWidth();
		BallShooter.HEIGHT = BallShooter.WIDTH * 16 / 9;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Assets.getInstance().dispose();
		BallShooter.font.dispose();
		BallShooter.shapeRenderer.dispose();

		this.getScreen().dispose();
	}
}
