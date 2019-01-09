package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Objects.PowerUps.SuperBall;

public class BlockStar extends Block {
    public BlockStar(World world, float length) {
        super(world, length);
        setColor(Color.ORANGE);
    }
}
