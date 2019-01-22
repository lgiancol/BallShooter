package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.PhysicsObject;

public class GameBall extends PhysicsObject {
    public int damageAmount = 1;
    private float radius;

    public GameBall(World world, Vector2 position, float radius, Vector2 dir) {
        this.radius = radius;
        color = Color.WHITE;
        name = "Ball";

        this.position = new Vector2(position.x - radius, position.y - radius);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.toWorldUnits(radius));
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

//        System.out.println("Ball angle: " + angle);

        body.setTransform(Constants.toWorldUnits(position.x), Constants.toWorldUnits(position.y), 0);

        if(dir.angle() <= 10) dir.setAngle(10);
        else if(dir.angle() >= 170) dir.setAngle(170);

        dir.scl(15f);
        body.applyForceToCenter(Constants.toScreenUnits(dir.x), Constants.toScreenUnits(dir.y), true);
    }

    @Override
    public void update(float delta) {
        if(body.getLinearVelocity().len() < 15f) body.setLinearVelocity(body.getLinearVelocity().nor().scl(15f));
        position.set(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));
    }

    @Override
    public void render(ShapeRenderer renderer, Batch batch) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);

        renderer.circle(position.x, position.y, radius);

        renderer.end();
    }
}