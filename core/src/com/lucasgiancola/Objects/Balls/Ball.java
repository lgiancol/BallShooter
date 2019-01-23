package com.lucasgiancola.Objects.Balls;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Ball extends Actor {

    private Texture tex;

    public Ball() {
        setColor(Color.WHITE);
        setWidth(100);
        setHeight(100);

        Pixmap pixmap = new Pixmap( (int) getWidth(), (int) getWidth(), Pixmap.Format.RGBA8888 );
        pixmap.setColor( getColor());
        pixmap.fillCircle((int) getWidth() / 2, (int) getWidth() / 2, (int) getWidth() / 2);
        tex = new Texture( pixmap );
        pixmap.dispose();

        // When we actually draw the ball, it is going to be moved so that the center of it is drawn at the point
        // so we need to make sure to push it over and down
        setPosition(getWidth() / 2, getWidth() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tex, getX() - getWidth() / 2, getY() - getWidth() / 2);
    }
}
