package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsModel {
    public static final SettingsModel instance = new SettingsModel();
    private static final String PREFS_NAME = "shooter";

    private SettingsModel() {
        Preferences p = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static SettingsModel getInstance() {
        return instance;
    }
}
