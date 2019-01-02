package com.lucasgiancola.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {
    private static final Assets instance = new Assets();
    public TextureAtlas gameAtlas;
    private BitmapFont gameFont;

    public static Assets getInstance() { return instance; }

    public void load() {
        gameFont = new BitmapFont(
                Gdx.files.internal("data/casual.fnt"),
                Gdx.files.internal("data/casual.png"), false);
        this.gameAtlas = new TextureAtlas(Gdx.files.internal("data/game.atlas"));
    }

    public BitmapFont getGameFont() { return this.gameFont; }

    public Drawable getDrawable(final String name) {
        TextureAtlas.AtlasRegion tmp = Assets.getInstance().gameAtlas.findRegion(name);
        return (new TextureRegionDrawable(tmp));
    }

    public void dispose() {
        this.gameFont.dispose();
        this.gameAtlas.dispose();
    }
}
