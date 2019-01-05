package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.PowerUps.SuperBall;

public class BlockSuperBall extends BlockPowerUp {

    public BlockSuperBall(World world, float length) {
        super(world, length);
        setColor(Color.GREEN);

        powerUp = new SuperBall(2, 7);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.update();

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(getColor());

        sr.rect(getX(), getY(), getWidth(), getHeight());

        // Ball in middle
        sr.setColor(Color.ORANGE);
        sr.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.BLACK);
        sr.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        sr.end();
        batch.begin();

        BallShooter.font.draw(
                batch,
                "" + this.getCurrentValue(),
                getX() + ((getWidth() - textLayout.width) / 2),
                getY() + ((getHeight() + textLayout.height) / 2));
    }
}
