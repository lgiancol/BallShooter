package com.lucasgiancola.Objects.Triggers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.PhysicsObject;

public class BottomTrigger extends PhysicsObject {
    private float width, height;

    public BottomTrigger(World world, Vector2 position, float width) {
        this.width = width;
        height = 2;
        this.position = new Vector2(position.x - (width / 2), position.y - (height / 2));

        color = Color.WHITE;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(width / 2), Constants.toWorldUnits(height / 2), new Vector2(0, 0), 0f);

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

        // Set the starting position of the block so we can update it according to physics
        body.setTransform(Constants.toWorldUnits(position.x), Constants.toWorldUnits(position.y), 0);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(ShapeRenderer renderer, Batch batch) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);

        renderer.rect(position.x, position.y,
                width / 2, height / 2,
                width, height,
                1.0f, 1.0f,
                body.getAngle());

        renderer.end();
    }
}
