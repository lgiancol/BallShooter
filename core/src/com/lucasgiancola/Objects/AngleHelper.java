package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AngleHelper extends Actor {
    private ShapeRenderer sr;
    private Vector2 start;
    private Vector2 end;

    private float angle = 90;

    public AngleHelper(Vector2 start) {
        this.sr = new ShapeRenderer();

        this.start = start;
    }

    public void draw(Batch batch, float parentAngle) {
        if(end == null) return;

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        sr.line(start, end);

        sr.end();
        batch.begin();
    }

    public void setAngle(float angle) {
        if(angle < 15) angle = 10;
        else if(angle > 165) angle = 170;

        this.angle = angle;

        float fx = (float) (3000 * Math.cos(this.angle * MathUtils.degreesToRadians));
        float fy = (float) (3000 * Math.sin(this.angle * MathUtils.degreesToRadians));

        this.end = new Vector2(this.start.x, this.start.y).add(fx, fy);
    }
}
