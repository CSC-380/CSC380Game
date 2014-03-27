package edu.oswego.tiltandtumble;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class Main {
	public static void main(String[] args) {
		
		new LwjglApplication(new TiltAndTumble(), "CSC 380 Project", 480, 320, true);
	}
}
