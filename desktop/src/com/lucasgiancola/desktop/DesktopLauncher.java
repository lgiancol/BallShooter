package com.lucasgiancola.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lucasgiancola.Application;
import com.lucasgiancola.BallShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Drop";
		config.width = Application.V_WIDTH;
		config.height = Application.V_HEIGHT;
		new LwjglApplication(new Application(), config);
	}
}
