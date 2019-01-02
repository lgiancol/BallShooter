package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lucasgiancola.Constants;

public class Wall extends Actor {
    private Body body;
    private int value = 0;
    private float angle = 0;
    private ShapeRenderer sr;

    public Wall(World world, int width, int height) {
        setName("Wall");
        setSize(width, height);
        setOrigin(getWidth() / 2, getHeight() / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.pixelsToBox(getWidth() / 2), Constants.pixelsToBox(getHeight() / 2), new Vector2(0, 0), 0f);

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = block;
        shapeDef.friction = 0f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = false;

        this.body.createFixture(shapeDef);
        block.dispose();

        sr = new ShapeRenderer();
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void hit(Ball ball) {
        setValue(getValue() - ball.getHitValue());

        System.out.println("Block Value: " + getValue());
    }

    @Override
    public void setPosition(float x, float y) {
        float scaledX = Constants.pixelsToBox(x);
        float scaledY = Constants.pixelsToBox(y);

        this.body.setTransform(new Vector2(scaledX, scaledY), this.angle + this.body.getAngle() * MathUtils.degreesToRadians); // Position and rotation of physics body
        super.setPosition(x-getWidth()/2, y-getHeight()/2); // Position of Image
    }

    private void update() {
        setPosition(
                Constants.boxToPixels(body.getPosition().x),
                Constants.boxToPixels(body.getPosition().y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.update();
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setProjectionMatrix(batch.getProjectionMatrix());

        sr.rect(getX(), getY(), getWidth(), getHeight());

        sr.end();
        batch.begin();

        super.draw(batch, parentAlpha);

    }

}
