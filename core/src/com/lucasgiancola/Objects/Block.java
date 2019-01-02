package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lucasgiancola.Constants;

public class Block extends Actor {
    private Body body;
    private int maxValue = 0;
    private int currentValue = 0;
    private float angle = 0;
    private float rotationSpeed = 0;
//    private
    private boolean done = false;
    private ShapeRenderer sr;

    private Vector3 color = new Vector3(1, 0, 0);

    public Block(World world, float length) {
        setName("Block");
        setSize(length, length);
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
        shapeDef.filter.categoryBits = Constants.CATEGORY_BLOCK;
        shapeDef.filter.maskBits = Constants.MASK_BLOCK;

        this.body.createFixture(shapeDef);
        block.dispose();

        sr = new ShapeRenderer();
    }

    public boolean shouldDestroy() {
        return this.done;
    }

    public int getCurrentValue() {
        return this.currentValue;
    }
    public void setCurrentValue(int value) { this.currentValue = value; }

    public int getValue() { return this.maxValue; }
    public void setValue(int value) {
        this.maxValue = this.currentValue = value;
    }

    public void hit(Ball ball) {
        setCurrentValue(getCurrentValue() - ball.getHitValue());
        color.x = (float) getCurrentValue() / (float) getValue();
        color.y = 1f - (float) getCurrentValue() / (float) getValue();

        done = getCurrentValue() <= 0;
    }

    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    @Override
    public void setPosition(float x, float y) {
        float scaledX = Constants.pixelsToBox(x);
        float scaledY = Constants.pixelsToBox(y);

        this.body.setTransform(new Vector2(scaledX, scaledY), this.angle + this.body.getAngle() * MathUtils.degreesToRadians); // Position and rotation of physics body
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2); // Position of Image
        super.setRotation(this.body.getAngle());
    }

    private void update() {
        this.angle += this.rotationSpeed;
        setPosition(
                Constants.boxToPixels(body.getPosition().x),
                Constants.boxToPixels(body.getPosition().y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.update();

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(color.x, color.y, color.z, 1);

        sr.rect(getX(), getY(), getWidth(), getHeight());

        sr.end();
        batch.begin();

        super.draw(batch, parentAlpha);
    }

    public Body getBody() {
        return this.body;
    }

}
