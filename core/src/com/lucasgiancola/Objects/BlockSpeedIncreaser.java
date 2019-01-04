package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.PowerUps.PowerUp;
import com.lucasgiancola.PowerUps.SpeedIncreaser;

public class BlockSpeedIncreaser extends BlockPowerUp {

    public BlockSpeedIncreaser(World world, float length) {
        super(world, length);
        setColor(Color.BLUE);

        powerUp = new SpeedIncreaser(0.025f, 7);
    }
}
