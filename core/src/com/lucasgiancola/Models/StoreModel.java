package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class StoreModel {
    private final String PREFS_NAME = "shooter-store";
    private int ballSpeedLevel = 0;

    public StoreModel() {
        Preferences p = Gdx.app.getPreferences(PREFS_NAME);

        if(p.contains("ballSpeedLevel")) {
            this.ballSpeedLevel = p.getInteger("ballSpeedLevel");
        }
    }

    public void increaseBallSpeedLevel() {
        this.ballSpeedLevel += 1;

        Preferences p = Gdx.app.getPreferences(PREFS_NAME);
        p.putInteger("ballSpeedLevel", this.ballSpeedLevel);
    }
}
