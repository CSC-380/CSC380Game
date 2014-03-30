package edu.oswego.tiltandtumble;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class Main {
	public static void main(String[] args) {

		new LwjglApplication(new TiltAndTumble(), "TiltAndTumble", 480, 320, true);
	}
}
