package com.lucasgiancola.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Rectangle {

    private Texture tex = null;
    private Vector2 pos;
    private float width, height;
    private Color col = Color.RED;
    private Color debugColour = Color.BLUE;
    private boolean debug = false;

    public Rectangle(float x, float y, float width, float height, int amount) {
        pos = new Vector2(x, y);
        this.width = width;
        this.height = height;

        System.out.println("Vector: " + pos);


//        createTexture(Color.BLUE);
    }

    private void createTexture(Color col) {
        Pixmap pix = new Pixmap((int)width, (int)height, Pixmap.Format.RGBA8888);

        if(col != null) {
            pix.setColor(col);
            pix.fillRectangle(0, 0, (int)width, (int)height);
        }

        if(this.debug) {
            pix.setColor(debugColour);
            pix.drawRectangle(0, 0, (int)width, (int)height);
            pix.drawRectangle(1, 1, (int)width - 2, (int)height - 2);
            pix.drawRectangle(2, 2, (int)width - 3, (int)height - 3);
        }

        tex = new Texture(pix);
        pix.dispose();
    }

    public void setDebug(boolean debug) {
        if(debug == !this.debug) {
            this.debug = debug;
        }
    }

    public void setColour(Color col) {
        this.col = col;

        createTexture(this.col);
    }

    public void draw(SpriteBatch batch) {
        if(tex == null) {
            createTexture(col);
        }

        batch.draw(tex, pos.x, pos.y, width, height);

    }
}
