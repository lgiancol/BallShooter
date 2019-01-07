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

        powerUp = new SuperBall(2, 3);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.update();

        batch.end();
        BallShooter.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        BallShooter.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        BallShooter.shapeRenderer.setColor(getColor());

        BallShooter.shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

        // Ball in middle
        BallShooter.shapeRenderer.setColor(Color.ORANGE);
        BallShooter.shapeRenderer.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        BallShooter.shapeRenderer.end();

        BallShooter.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        BallShooter.shapeRenderer.setColor(Color.BLACK);
        BallShooter.shapeRenderer.circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), Math.max(textLayout.width, textLayout.height));

        BallShooter.shapeRenderer.end();
        batch.begin();

        BallShooter.font.draw(
                batch,
                "" + this.getCurrentValue(),
                getX() + ((getWidth() - textLayout.width) / 2),
                getY() + ((getHeight() + textLayout.height) / 2));
    }
}
