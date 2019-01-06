package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

public class BlockFactory {

    public static Block createRandomBlock(World addTo) {
        if (MathUtils.randomBoolean(0.4f)) {
            return new BlockSpeedIncreaser(addTo, Block.blockWidth);
        } else if (MathUtils.randomBoolean(0.4f)) {
            return new BlockSuperBall(addTo, Block.blockWidth);
        } else {
            return new Block(addTo, Block.blockWidth);
        }
    }
}
