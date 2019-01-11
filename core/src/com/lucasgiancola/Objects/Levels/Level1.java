package com.lucasgiancola.Objects.Levels;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.lucasgiancola.Objects.Blocks.GameBlock;

public class Level1 extends BaseLevel {
    private GameBlock block, block1;

    public Level1(float levelWidth, float levelHeight) {
        super(levelWidth, levelHeight);

        block = new GameBlock(levelWorld, new Vector2(xOffset, yOffset));
        block.startMove();

//        block1 = new GameBlock(levelWorld, new Vector2(xOffset, yOffset));
    }

    @Override
    public void update(float delta) {
        block.update(delta);
    }

    @Override
    public void render() {
        // This top section is temporary
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect((-worldWidth / 2) + 1, (-worldHeight / 2) + 1, worldWidth - 1, worldHeight - 1);
        renderer.end();

        // Render all the objects
        block.render(renderer);
    }
}
