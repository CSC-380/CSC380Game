package edu.oswego.maestri.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		
		new LwjglApplication(new TiltAndTumble(), "CSC 380 Project", 480, 320, true);
	}
}
