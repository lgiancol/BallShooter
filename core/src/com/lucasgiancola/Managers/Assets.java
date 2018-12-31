package com.lucasgiancola.Managers;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
    public static AssetManager assetManager = new AssetManager();
    public static final AssetDescriptor<TextureAtlas> uiAtlas =
            new AssetDescriptor<TextureAtlas>("skins/uiskin.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Skin> uiSkin =
            new AssetDescriptor<Skin>("skins/uiskin.atlas", Skin.class,
                    new SkinLoader.SkinParameter("skins/uiskin.atlas"));

    public static final AssetDescriptor<TextureAtlas> spriteAtlas =
            new AssetDescriptor<TextureAtlas>("skins/temp/temp.atlas", TextureAtlas.class);

    public static void load() {
        assetManager.load(uiAtlas);
//        assetManager.load(uiSkin);
        assetManager.load(spriteAtlas);
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
