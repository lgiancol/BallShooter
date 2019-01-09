package com.lucasgiancola.Screens;

import com.lucasgiancola.BallShooter;
import com.lucasgiancola.Models.PlayerModel;
import com.lucasgiancola.Models.StoreModel;

public class StoreScreen extends AbstractScreen{
    private StoreModel store;
    private PlayerModel player = PlayerModel.instance;

    public StoreScreen(BallShooter ballShooter) {
        super(ballShooter);

        this.store = new StoreModel();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
