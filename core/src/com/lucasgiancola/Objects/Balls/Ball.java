package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.BaseObject;
import com.lucasgiancola.Objects.Levels.Level;

public class Ball extends BaseObject {
    private float shootAngle = 180f;
    private float moveSpeed = 2500f;
    private int hitValue = 1;
    private static float radius = 0;

    public Ball(World world) {
        super();
        setName("Ball");
        setRadius(radius);
        setOrigin(getWidth() / 2, getHeight() / 2);
        setColor(Color.WHITE);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);

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

    public float getShootAngle() {
        return shootAngle;
    }
    public void setShootAngle(float angle) {
        if(angle < 15) angle = 10;
        else if(angle > 165) angle = 170;

        this.shootAngle = angle;
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

    @Override
    public void update() {
        setPosition(
                Constants.boxToPixels(body.getPosition().x),
                Constants.boxToPixels(body.getPosition().y));
        setRotation( this.body.getAngle() * MathUtils.radDeg);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        this.update();

        batch.end();
        Level.renderer.begin(ShapeRenderer.ShapeType.Filled);
        Level.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        Level.renderer.setColor(getColor());

        Level.renderer.circle(getX(), getY(), Ball.getRadius());

        Level.renderer.end();
        batch.begin();
    }

}
