package com.lucasgiancola.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Managers.Assets;

public class Ball extends Actor {
    private Body body;
    private float shootAngle = 180f;
    private float moveSpeed = 2500f;
    private int hitValue = 1;
    private static float radius = 0;
    private ShapeRenderer sr;

    public Ball(World world) {
        setName("Ball");
        setRadius(radius);
        setOrigin(getWidth() / 2, getHeight() / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);
        this.body.setBullet(true);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.pixelsToBox(Ball.radius));
        circleShape.setPosition(new Vector2(0, 0));

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = circleShape;
        shapeDef.friction = 0f;
        shapeDef.density = 0f;
        shapeDef.restitution = 1f;
        shapeDef.isSensor = false;
        shapeDef.filter.categoryBits = Constants.CATEGORY_BALL;
        shapeDef.filter.maskBits = Constants.MASK_BALL;

        this.body.createFixture(shapeDef);
        circleShape.dispose();

        sr = new ShapeRenderer();
    }

    private void update() {
        setPosition(
                Constants.boxToPixels(body.getPosition().x),
                Constants.boxToPixels(body.getPosition().y));
        setRotation( this.body.getAngle() * MathUtils.radDeg);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        this.update();

        batch.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setColor(Color.WHITE);

        sr.circle(getX(), getY(), Ball.getRadius());

        sr.end();
        batch.begin();

        super.draw(batch, parentAlpha);
    }

    public Body getBody() {
        return this.body;
    }

    public static float getRadius() {
        return Ball.radius;
    }

    public static void setRadius(float radius) {
        Ball.radius = radius;
    }

    public int getHitValue() {
        return this.hitValue;
    }

    public void setHitValue(int hitValue) {
        this.hitValue = hitValue;
    }

    @Override
    public void setPosition(float x, float y) {
        float scaledX = Constants.pixelsToBox(x);
        float scaledY = Constants.pixelsToBox(y);

        this.body.setTransform(new Vector2(scaledX, scaledY), this.body.getAngle()); // Position and rotation of physics body
        super.setPosition(x-getWidth()/2, y-getHeight()/2); // Position of Image
    }

    public void launch() {
        float fx = (float) (this.moveSpeed * Math.cos(shootAngle * MathUtils.degreesToRadians));
        float fy = (float) (this.moveSpeed * Math.sin(shootAngle * MathUtils.degreesToRadians));
        this.body.applyForceToCenter(fx, fy, true);
    }

    public float getShootAngle() {
        return shootAngle;
    }

    public void setShootAngle(float angle) {
        this.shootAngle = angle;
    }

}
