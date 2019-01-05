package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Objects.PowerUps.PowerUp;

public class BlockPowerUp extends Block {
    protected PowerUp powerUp;

    public BlockPowerUp(World world, float length) {
        super(world, length);
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }
}
