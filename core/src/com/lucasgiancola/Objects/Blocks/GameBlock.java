package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.PhysicsObject;

import java.util.ArrayList;

public class GameBlock extends PhysicsObject {
    private int health = 10;
    private ArrayList<GameBlockPulse> pulses;

    public GameBlock(World world, Vector2 position) {
        size = 200;
        this.position = new Vector2(position.x - (size / 2), position.y - (size / 2));
        pulses = new ArrayList<GameBlockPulse>();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(size / 2), Constants.toWorldUnits(size / 2), new Vector2(0, 0), 0f);

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = block;
        shapeDef.friction = 1f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = false;
        shapeDef.filter.categoryBits = Constants.CATEGORY_BLOCK;
        shapeDef.filter.maskBits = Constants.MASK_BLOCK;

        body.createFixture(shapeDef);
        block.dispose();

        // Set the starting position of the block so we can update it according to physics
        body.setTransform(Constants.toWorldUnits(position.x), Constants.toWorldUnits(position.y), 0f);
    }

    public void takeDamage(int amount) {
        health -= amount;

        pulses.add(new GameBlockPulse(position, size));
    }

    @Override
    public void update(float delta) {
        for(int i = pulses.size() - 1; i >= 0; i--) {
            GameBlockPulse pulse = pulses.get(i);
            if(pulse.canRemove) {
                pulses.remove(i);
                continue;
            }

            pulse.update(delta);
        }

        System.out.println("Pulses: " + pulses.size());

        // Set the position to be the screen position of the body in the world
        position.set(Constants.toScreenUnits(body.getPosition().x) - (size / 2), Constants.toScreenUnits(body.getPosition().y) - (size / 2));
    }

    @Override
    public void render(ShapeRenderer renderer) {

        for(GameBlockPulse pulse: pulses) {
            pulse.render(renderer);
        }

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLUE);

        renderer.rect(position.x, position.y, size, size);

        renderer.end();
    }

    @Override
    public String toString() {
        return "Position: [" + position.x + ", " + position.y + "] Dimensions: [" + size + ", " + size + "]";
    }
}
