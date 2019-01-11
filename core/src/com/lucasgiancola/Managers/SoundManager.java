package com.lucasgiancola.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    public static final SoundManager instance = new SoundManager();

    private Map<String, Sound> sounds = new HashMap<String, Sound>();

    public static final String BALL_HIT = "sounds/hit.ogg";

    public void init() {
        this.sounds.put(BALL_HIT, Gdx.audio.newSound( Gdx.files.internal(BALL_HIT)));

    }

    public void playSound(final String sound) {
//        Sound s = this.sounds.get(sound);
//        if (s != null) {
//            s.play();
//        }
    }
}
