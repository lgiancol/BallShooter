package com.lucasgiancola.Objects.Triggers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Constants;
import com.lucasgiancola.Objects.BaseObject;

public class Destroyer extends BaseObject {

    public Destroyer(World world, int width, int height) {
        super();
        setName("Destroyer");
        setSize(width, height);
        setOrigin(getWidth() / 2, getHeight() / 2);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.body = world.createBody(bodyDef);
        this.body.setUserData(this);

        PolygonShape block = new PolygonShape();
        block.setAsBox(Constants.toWorldUnits(getWidth() / 2), Constants.toWorldUnits(getHeight() / 2), new Vector2(0, 0), 0f);

        FixtureDef shapeDef = new FixtureDef();
        shapeDef.shape = block;
        shapeDef.friction = 0f;
        shapeDef.density = 0f;
        shapeDef.restitution = 0f;
        shapeDef.isSensor = true;
        shapeDef.filter.categoryBits = Constants.CATEGORY_DESTROYER;
        shapeDef.filter.maskBits = Constants.MASK_DESTROYER;

        this.body.createFixture(shapeDef);
        block.dispose();
    }

    @Override
    public void setPosition(float x, float y) {
        float scaledX = Constants.toWorldUnits(x);
        float scaledY = Constants.toWorldUnits(y);

        this.body.setTransform(new Vector2(scaledX, scaledY), this.body.getAngle() * MathUtils.degreesToRadians); // Position and rotation of physics body
        super.setPosition(x - getWidth() / 2, y - getHeight() / 2); // Position of Image

    }

    public void update() {
        setPosition(
                Constants.toScreenUnits(body.getPosition().x),
                Constants.toScreenUnits(body.getPosition().y));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.update();
        batch.end();

        BallShooter.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        BallShooter.shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        BallShooter.shapeRenderer.setColor(Color.GREEN);

        BallShooter.shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());

        BallShooter.shapeRenderer.end();
        batch.begin();

    }

}
