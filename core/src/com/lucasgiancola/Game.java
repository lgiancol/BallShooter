package com.lucasgiancola;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.lucasgiancola.Managers.Assets;
import com.lucasgiancola.Screens.GameScreen;

public class Game extends com.badlogic.gdx.Game {
	public SpriteBatch batch;
	public BitmapFont font;

	public static final Input input = new Input();
	
	@Override
	public void create () {
//		Assets.load();
//		Assets.assetManager.finishLoading();

		batch = new SpriteBatch();
		font = new BitmapFont();

		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();

		this.getScreen().dispose();
		Assets.dispose();
	}
}
