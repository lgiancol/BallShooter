package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.GameBaseObject;

public class GameBall extends GameBaseObject {
    private float radius = 100;

    public GameBall(World world, Vector2 position) {

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

        body.setTransform(Constants.toWorldUnits(position.x), Constants.toWorldUnits(position.y), 0f);

        body.applyForceToCenter(Constants.toScreenUnits(0), Constants.toScreenUnits(0.8f), true);
    }

    @Override
    public void update(float delta) {
        position.set(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);

        renderer.circle(position.x, position.y, radius);

        renderer.end();
    }
}
