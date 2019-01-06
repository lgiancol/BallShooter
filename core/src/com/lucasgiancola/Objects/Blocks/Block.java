package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.Balls.Ball;
import com.lucasgiancola.Objects.BaseObject;
import com.lucasgiancola.Objects.Levels.Level;

public class Block extends BaseObject {
    private int maxValue = 0;
    private int currentValue = 0;
    private float angle = 0;
    private float rotationSpeed = 0;
    private float moveSpeed = 0.6f;
    private boolean done = false;
    protected GlyphLayout textLayout;

    protected int row;
    protected int col;

    public static float blockWidth = 0;
    public static float blockOffset = 20;

    public Block(World world, float length) {
        super();
        setName("Block");
        setSize(length, length);
        setOrigin(getWidth() / 2, getHeight() / 2);
        textLayout = new GlyphLayout(BallShooter.font, "" + this.getCurrentValue());
        setColor(Color.RED);

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

        textLayout = new GlyphLayout(BallShooter.font, "" + this.currentValue);
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
//        blockColour.x = (float) getCurrentValue() / (float) getValue();
//        blockColour.y = 1f - (float) getCurrentValue() / (float) getValue();

        done = getCurrentValue() <= 0;
    }

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;

        int minValue = ((this.row - 1) * 3) + 1;
        int maxValue = minValue + 10;

        this.setValue(MathUtils.random(minValue, maxValue));

        float x = col * Block.blockWidth + (Block.blockWidth / 2) + ((col + 1) * Block.blockOffset);
        float y = BallShooter.HEIGHT + (row * Block.blockWidth) + (row * Block.blockOffset);

        this.setPosition(x, y);
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
        Level.renderer.begin(ShapeRenderer.ShapeType.Filled);
        Level.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        Level.renderer.setColor(getColor());

        Level.renderer.rect(getX(), getY(), getWidth(), getHeight());
        Level.renderer.end();
        batch.begin();

        BallShooter.font.draw(
                batch,
                "" + this.getCurrentValue(),
                getX() + ((getWidth() - textLayout.width) / 2),
                getY() + ((getHeight() + textLayout.height) / 2));

    }
}
