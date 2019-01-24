package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameObject extends Actor {
    protected Texture tex;
    public Body body;

    public void dispose() {
        tex.dispose();
        remove();
        body.setUserData(null);
        body.getWorld().destroyBody(body);
    }
}
