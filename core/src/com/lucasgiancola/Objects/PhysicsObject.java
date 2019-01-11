package com.lucasgiancola.Objects;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class PhysicsObject extends GameBaseObject {
    public Body body;
    public boolean canDestroyBody = false;
    public boolean isBodyDestroyed = false;
    public String name = "Physics Object";

    public void dispose() {
        body.setUserData(null);
        body.getWorld().destroyBody(body);
    }

    public String toString() {
        return "[ " + name + " ]";
    }
}
