package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;

public class Block extends BaseObject {
    private int maxValue = 0;
    private int currentValue = 0;
    private float angle = 0;
    private float rotationSpeed = 0;
    private float moveSpeed = 1.5f;
    private boolean done = false;
    private float textOffset = 5;

    public static float blockWidth = 0;

    private Vector3 color = new Vector3(1, 0, 0);

    public Block(World world, float length) {
        super();
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
        shapeDef.friction = 1f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = false;
        shapeDef.filter.categoryBits = Constants.CATEGORY_BLOCK;
        shapeDef.filter.maskBits = Constants.MASK_BLOCK;

        this.body.createFixture(shapeDef);
        block.dispose();

        move();
    }

    public int getCurrentValue() { return this.currentValue; }
    public void setCurrentValue(int value) {
        this.currentValue = value;

        this.textOffset = 5 * String.valueOf(this.currentValue).length();
    }

    public int getValue() { return this.maxValue; }
    public void setValue(int value) { this.maxValue = this.currentValue = value; }

    public float getRotationSpeed() { return this.rotationSpeed; }
    public void setRotationSpeed(float speed) { this.rotationSpeed = speed; }

    private void move() {
        float fx = (float) (this.moveSpeed * Math.cos(270 * MathUtils.degreesToRadians));
        float fy = (float) (this.moveSpeed * Math.sin(270 * MathUtils.degreesToRadians));
        this.body.setLinearVelocity(fx, fy);
    }

    public boolean shouldDestroy() {
        return this.done;
    }

    public void hit(Ball ball) {
        setCurrentValue(getCurrentValue() - ball.getHitValue());
        color.x = (float) getCurrentValue() / (float) getValue();
        color.y = 1f - (float) getCurrentValue() / (float) getValue();

        done = getCurrentValue() <= 0;
    }

    @Override
    public void setPosition(float x, float y) {
        float scaledX = Constants.pixelsToBox(x);
        float scaledY = Constants.pixelsToBox(y);

        this.body.setTransform(new Vector2(scaledX, scaledY), this.angle + this.body.getAngle() * MathUtils.degreesToRadians); // Position and rotation of physics body
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2); // Position of Image
        super.setRotation(this.body.getAngle());
    }

    public void update() {
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

        BallShooter.font.draw(
                batch,
                "" + this.getCurrentValue(),
                getX() + (getWidth() / 2) - textOffset,
                getY() + (getHeight() / 2) + (BallShooter.font.getLineHeight() / 2));

    }
}
