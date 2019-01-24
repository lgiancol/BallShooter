package com.lucasgiancola.Objects.Walls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.GameObject;

public class ObjectDespawner extends GameObject {

    public ObjectDespawner(World world, Vector2 position, float width, float height) {
        setColor(Color.RED);
        setWidth(width);
        setHeight(height);

        // Create the texture for the ball
        // TODO: Make this constant so we don't need to create a new one every time
        Pixmap pixmap = new Pixmap( (int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888 );
        pixmap.setColor( getColor());
        pixmap.fillRectangle(0, 0, (int) getWidth(), (int) getHeight());
        tex = new Texture( pixmap );
        pixmap.dispose();

        // Create a body for the ball add it to the world that was provided
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(getWidth() / 2),Constants.toWorldUnits(getHeight() / 2), new Vector2(Constants.toWorldUnits(getWidth() / 2), Constants.toWorldUnits(getHeight() / 2)), 0f);

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = block;
        shapeDef.friction = 1f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = true;
        shapeDef.filter.categoryBits = Constants.CATEGORY_DESTROYER;
        shapeDef.filter.maskBits = Constants.MASK_DESTROYER;

        body.createFixture(shapeDef);
        block.dispose();

        // Set the initial position
        body.setTransform(Constants.toWorldUnits(position.x - getWidth() / 2), Constants.toWorldUnits(position.y - getHeight() / 2), 0);
        setPosition(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tex, getX(), getY());
    }
}
