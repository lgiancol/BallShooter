package com.lucasgiancola.Objects;

import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.PowerUps.SuperBall;

public class BlockSuperBall extends BlockPowerUp {

    public BlockSuperBall(World world, float length) {
        super(world, length);

        this.blockColour.set(1f, 1f, 1f);
        powerUp = new SuperBall(2, 7);
    }
}
