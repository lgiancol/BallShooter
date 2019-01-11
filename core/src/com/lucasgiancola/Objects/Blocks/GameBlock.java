package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.GameBaseObject;

public class GameBlock extends GameBaseObject {

    public GameBlock(World world, Vector2 position) {
        width = 20;
        height = 20;
        this.position = position;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(width / 2), Constants.toWorldUnits(height / 2), new Vector2(0, 0), 0f);

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = block;
        shapeDef.friction = 1f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = false;
        shapeDef.filter.categoryBits = Constants.CATEGORY_BLOCK;
        shapeDef.filter.maskBits = Constants.MASK_BLOCK;

        body.createFixture(shapeDef);

        // Set the starting position of the block so we can update it according to physics
        body.setTransform(Constants.toWorldUnits(this.position.x), Constants.toWorldUnits(this.position.y), 0f);
    }

    // Temp
    public void startMove() {
        body.setLinearVelocity(0, 1);
    }

    @Override
    public void update(float delta) {
        // Set the position to be the screen position of the body in the world
        position.set(Constants.toScreenUnits(body.getPosition().x), Constants.toScreenUnits(body.getPosition().y));
    }

    @Override
    public void render(ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLUE);

        renderer.rect(position.x, position.y, width, height);

        renderer.end();
    }

    @Override
    public String toString() {
        return "Position: [" + position.x + ", " + position.y + "] Dimensions: [" + width + ", " + height + "]";
    }
}
