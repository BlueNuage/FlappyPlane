package com.BlueNuageStudios.FlappyPlane;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Flappy Plane";
		cfg.useGL20 = true;
		//cfg.fullscreen = true;
		cfg.foregroundFPS = 60;
		cfg.height = 1280;
		cfg.width = 1920;
		
		new LwjglApplication(new MainClass(), cfg);
	}
}
