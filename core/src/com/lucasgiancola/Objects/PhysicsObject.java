package com.lucasgiancola.Objects;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PhysicsObject extends GameBaseObject {
    public Body body;
    public boolean canDestroy = false;
    public boolean isDestroyed = false;

    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
