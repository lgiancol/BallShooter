package com.lucasgiancola.Objects.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.Map;

public class GUI {
    private Stage stage;

    private Map<String, Actor> elements;

    public GUI(Stage stage) {
        this.stage = stage;

        this.elements = new HashMap<String, Actor>();
    }

    public void addElement(String name, Actor toAdd) {
        this.stage.addActor(toAdd);

        elements.put(name, toAdd);
    }

    public void updateLabel(String name, String text) {
        ((Label) this.elements.get(name)).setText(text);
    }
}
