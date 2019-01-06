package com.lucasgiancola.Objects.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lucasgiancola.BallShooter;

public class Label extends Actor {
    private String text;

    public Label(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        BallShooter.font.draw(batch, this.text, getX(), getY());

    }
}
