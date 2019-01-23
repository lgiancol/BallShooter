package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.GameObject;

public class Ball extends GameObject {

    public Ball(World world, Vector2 position) {
        setColor(Color.WHITE);
        setWidth(100); // Width will be used as the half radius

        // Create the texture for the ball
        // TODO: Make this constant so we don't need to create a new one every time
        Pixmap pixmap = new Pixmap( (int) getWidth(), (int) getWidth(), Pixmap.Format.RGBA8888 );
        pixmap.setColor( getColor());
        pixmap.fillCircle((int) getWidth() / 2, (int) getWidth() / 2, (int) getWidth() / 2);
        tex = new Texture( pixmap );
        pixmap.dispose();

        // Create a body for the ball add it to the world that was provided
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Constants.toWorldUnits(getWidth() / 2));
        circleShape.setPosition(new Vector2(Constants.toWorldUnits(getWidth() / 2), Constants.toWorldUnits(getWidth() / 2)));

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

        // Set the initial position
        body.setTransform(Constants.toWorldUnits(position.x - getWidth() / 2), Constants.toWorldUnits(position.y - getWidth() / 2), 0);
        setPosition(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));

        Vector2 dir = new Vector2(0, 1);
        dir.nor();
        dir.setAngle(45);
        dir.scl(5);
        body.applyForceToCenter(Constants.toScreenUnits(dir.x), Constants.toScreenUnits(dir.y), true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));
        batch.draw(tex, getX(), getY());
    }
}
