package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lucasgiancola.Objects.GameBaseObject;

public class GameBlockPulse extends GameBaseObject {
    private float maxDuration = 0.25f; // Lasts for 1 second
    private float currentDuration = 0;
    public boolean canRemove = false;
    private float maxSize = 0, startSize = 0, length = 0;
    private float angle = 0;

    public GameBlockPulse(GameBlock block) {
        length = startSize = block.length;
        angle = block.body.getAngle();
        color = block.color.cpy();

        maxSize = length * 1.5f; // 15% bigger than the original block
        this.position = new Vector2(block.position.x, block.position.y);
    }

    @Override
    public void update(float delta) {
        currentDuration += delta;

        float progress = (currentDuration / maxDuration);

        length = MathUtils.lerp(startSize, maxSize, progress);
        color.a = MathUtils.lerp(1, 0, progress);

        canRemove = currentDuration >= maxDuration;
    }

    @Override
    public void render(ShapeRenderer renderer, Batch batch) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);

        renderer.rect(position.x - ((length - startSize) / 2), position.y - ((length - startSize) / 2), length, length);

        renderer.end();
    }
}
