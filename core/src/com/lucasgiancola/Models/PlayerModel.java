package com.lucasgiancola.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PlayerModel {
    public static final PlayerModel instance = new PlayerModel();
    private final String PREFS_NAME = "shooter-player";

    // Player variables
    public int starCount = 0;
    public float ballDeltaTime = 0.5f;

    private PlayerModel() {
        Preferences p = Gdx.app.getPreferences(PREFS_NAME);

        if(p.contains("starCount")) {
            this.starCount = p.getInteger("starCount");
        }

        if(p.contains("ballDeltaTime")) {
            this.ballDeltaTime = p.getFloat("ballDeltaTime");
        }
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
        Preferences p =  Gdx.app.getPreferences( PREFS_NAME );
        p.putInteger("starCount", this.starCount);
        p.flush();
    }

    public void addStar() {
        this.setStarCount(this.starCount + 1);
    }
}
