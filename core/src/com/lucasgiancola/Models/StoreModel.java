package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class StoreModel {
    private final String PREFS_NAME = "shooter-store";

    public StoreModel() {
        Preferences p = Gdx.app.getPreferences(PREFS_NAME);
    }
}
