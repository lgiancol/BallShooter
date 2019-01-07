package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.physics.box2d.World;

public class BallFactory {
    public static Ball baseBall(World addToo) {
        return new Ball(addToo);
    }
}
