package com.lucasgiancola;

import com.badlogic.gdx.Game;

import com.lucasgiancola.Managers.Assets;
import com.lucasgiancola.Screens.GameScreen;

public class BallShooter extends Game {
	public static final float TGT_WIDTH = 1920;
	public static final float TGT_HEIGHT = TGT_WIDTH / 16 * 9;

	private void showGameScreen() {
		this.setScreen(new GameScreen(this));
	}
	
	@Override
	public void create () {
		Assets.getInstance().load(); // Will load all necessary assets for the game

		this.showGameScreen();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Assets.getInstance().dispose();

		this.getScreen().dispose();
	}
}
