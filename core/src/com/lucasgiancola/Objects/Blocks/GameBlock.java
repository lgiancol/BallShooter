package com.lucasgiancola.Objects.Blocks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.PhysicsObject;

import java.util.ArrayList;

public class GameBlock extends PhysicsObject {
    private int baseHealth = 100;
    public int health = baseHealth;
    private ArrayList<GameBlockPulse> pulses;
    private Vector2 velocity;
    private GlyphLayout layout;
    public float length;
//    private Color target;

    public GameBlock(World world, Vector2 position, float length) {
        this.length = length;
        this.position = new Vector2(position.x - (length / 2), position.y - (length / 2));
        pulses = new ArrayList<GameBlockPulse>();
        color = new Color(0.9f, 0.7f, 0.2f, 1f);
//        target = new Color(0.4f, 0.1f, 0.1f, 1f);
        velocity = new Vector2(0, -Constants.toWorldUnits(75f));
        layout = new GlyphLayout(BallShooter.font, "" + health);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(length / 2), Constants.toWorldUnits(length / 2), new Vector2(0, 0), 0f);

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
        body.setTransform(Constants.toWorldUnits(position.x), Constants.toWorldUnits(position.y), 0);
        body.setLinearVelocity(velocity);
    }

    public void takeDamage(int amount) {
        // Reduce the health by the amount and update the text
        health -= amount;
        layout.setText(BallShooter.font, "" + health);

        // Update the color of the block
//        color.lerp(target, (float) ((baseHealth - health) / baseHealth));
//        color.a = 1;

        // Add a new pulse to the block
        pulses.add(new GameBlockPulse(this));

        // Set if the block's physics body can be destroyed
        canDestroyBody = health <= 0;
    }

    @Override
    public void update(float delta) {
        for(int i = pulses.size() - 1; i >= 0; i--) {
            GameBlockPulse pulse = pulses.get(i);
            pulse.update(delta);

            // If the pulse can be removed
            if(pulse.canRemove) {
                // Remove it
                pulses.remove(i);
            }
        }

        canRemoveGraphic = pulses.isEmpty();

        // Set the position to be the screen position of the body in the world
        position.set(Constants.toScreenUnits(body.getPosition().x) - (length / 2), Constants.toScreenUnits(body.getPosition().y) - (length / 2));
        body.getTransform().setRotation(body.getTransform().getRotation() + 0.5f);
//        body.setTransform(body.getPosition(), body.getAngle());
    }

    @Override
    public void render(ShapeRenderer renderer, Batch batch) {

        // Render pulses first since they need to be behind the actual block
        Gdx.gl.glEnable(GL20.GL_BLEND); // This will make sure we can render transparency for the pulses
        for(GameBlockPulse pulse: pulses) {
            pulse.render(renderer, batch);
        }

        // Only render the block if it has health
        if(health > 0) {
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(color);

            renderer.rect(position.x, position.y,
                    length / 2, length / 2,
                    length, length,
                    1.0f, 1.0f,
                    body.getAngle());

            renderer.end();

            batch.begin();
            BallShooter.font.draw(batch, layout, position.x + (length / 2) - (layout.width / 2), position.y + (length / 2) + (layout.height / 2));
            batch.end();
        }
    }
}
