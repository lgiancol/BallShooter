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
        setWidth(20);
        setHeight(20);
        setPosition(200, 0);

        System.out.println("X: " + getX() + ", Y: " + getY() + " | W: " + getWidth() + ", " + getHeight());

        Pixmap pixmap = new Pixmap( (int) getWidth(), (int) getHeight(), Pixmap.Format.RGBA8888 );
        pixmap.setColor( getColor());
        pixmap.drawRectangle(0, 0, (int) getWidth(), (int) getHeight());
        tex = new Texture( pixmap );
        pixmap.dispose();


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tex, getX(), getY());
    }
}
