package com.lucasgiancola;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lucasgiancola.Managers.Assets;
import com.lucasgiancola.Screens.GameScreen;

public class BallShooter extends Game {
	public static float WIDTH = 9;
	public static float HEIGHT = 16;

	public static BitmapFont font;
	public static ShapeRenderer shapeRenderer;

	private void showGameScreen() {
		this.setScreen(new GameScreen(this));
	}
	
	@Override
	public void create () {
		Assets.getInstance().load(); // Will load all necessary assets for the game
		updateDimensions();

		BallShooter.font = new BitmapFont(Gdx.files.internal("data/casual.fnt"));
		BallShooter.shapeRenderer = new ShapeRenderer();

		this.showGameScreen();
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
