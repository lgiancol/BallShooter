package com.lucasgiancola.Objects;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PhysicsObject extends GameBaseObject {
    public Body body;
    public boolean canDestroyBody = false;
    public boolean isBodyDestroyed = false;
    public boolean canRemoveGraphic = false;

    public void dispose() {
        body.getWorld().destroyBody(body);
    }
}
