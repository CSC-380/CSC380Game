package edu.oswego.tiltandtumble.levels;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import edu.oswego.tiltandtumble.worldObjects.Ball;
public class BallController  {
	


	enum MyKeys {
		LEFT, RIGHT, UP, DOWN
	}



	static Map<MyKeys, Boolean> keys = new HashMap<BallController.MyKeys, Boolean>();
	static {
		keys.put(MyKeys.LEFT, false);
		keys.put(MyKeys.RIGHT, false);
		keys.put(MyKeys.UP, false);
		keys.put(MyKeys.DOWN, false);
	};


	private final boolean useAccelerometer;
	//private boolean useOnScreenDPad;
	private Ball ball;

	private float tiltX = 0;
	private float tiltY = 0;

	private int keyX = 0;
	private int keyY = 0;

	public BallController(boolean useAccelerometer) {
		this.useAccelerometer = useAccelerometer;


	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}


	public void update() {
		if (useAccelerometer) {
			// accelerometer is reversed from screen coordinates, we are in landscape mode
			tiltX = Gdx.input.getAccelerometerY() * 0.001f;
			tiltY = Gdx.input.getAccelerometerX() * -0.001f;
		}
		else {
			if (keys.get(MyKeys.UP)) {
				decrementY();
			}
			else if (keys.get(MyKeys.DOWN)) {
				incrementY();
			}
			if (keys.get(MyKeys.LEFT)) {
				decrementX();
			}
			else if (keys.get(MyKeys.RIGHT)) {
				incrementX();
			}
			tiltX = keyX * 0.001f;
			tiltY = keyY * -0.001f;
		}
		if (ball != null) {
			ball.applyLinearImpulse(tiltX, tiltY);
		}
	}

	public float getX() {
		return tiltX;
	}

	public float getY() {
		return tiltY;
	}

	private void incrementX() {
		if (keyX >= 10) {
			keyX = 10;
		}
		else {
			keyX += 1;
		}
	}

	private void decrementX() {
		if (keyX <= -10) {
			keyX = -10;
		}
		else {
			keyX -= 1;
		}
	}

	private void incrementY() {
		if (keyY >= 10) {
			keyY = 10;
		}
		else {
			keyY += 1;
		}
	}

	private void decrementY() {
		if (keyY <= -10) {
			keyY = -10;
		}
		else {
			keyY -= 1;
		}
	}




	// ** Key presses and touches **************** //

	public void leftPressed() {
		keys.get(keys.put(MyKeys.LEFT, true));

	}

	public void rightPressed() {
		keys.get(keys.put(MyKeys.RIGHT, true));
	}

	public void upPressed() {
		keys.get(keys.put(MyKeys.UP, true));
	}
	public void downPressed(){
		keys.get(keys.put(MyKeys.DOWN, true));
	}


	public void leftReleased() {
		keys.get(keys.put(MyKeys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(MyKeys.RIGHT, false));
	}

	public void upReleased() {
		keys.get(keys.put(MyKeys.UP, false));
	}
	public void downReleased() {
		keys.get(keys.put(MyKeys.DOWN, false));
	}

}
