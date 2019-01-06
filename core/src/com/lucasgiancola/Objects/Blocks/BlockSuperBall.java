package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Objects.Levels.Level;
import com.lucasgiancola.Objects.PowerUps.SuperBall;

public class BlockSuperBall extends BlockPowerUp {

    public BlockSuperBall(World world, float length) {
        super(world, length);
        setColor(Color.GREEN);

        powerUp = new SuperBall(2, 3);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.update();

        batch.end();
        Level.renderer.begin(ShapeRenderer.ShapeType.Filled);
        Level.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        Level.renderer.setColor(getColor());

        Level.renderer.rect(getX(), getY(), getWidth(), getHeight());

        // Ball in middle
        Level.renderer.setColor(Color.ORANGE);
        Level.renderer.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        Level.renderer.end();

        Level.renderer.begin(ShapeRenderer.ShapeType.Line);
        Level.renderer.setColor(Color.BLACK);
        Level.renderer.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        Level.renderer.end();
        batch.begin();

        BallShooter.font.draw(
                batch,
                "" + this.getCurrentValue(),
                getX() + ((getWidth() - textLayout.width) / 2),
                getY() + ((getHeight() + textLayout.height) / 2));
    }
}
