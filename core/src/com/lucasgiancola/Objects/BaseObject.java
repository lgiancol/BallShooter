package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BaseObject extends Actor {
    protected Body body;

    protected abstract void update();

    public Body getBody() {
        return this.body;
    }
    public void dispose() {
        this.body.setUserData(null);

        this.remove();
    }
}
