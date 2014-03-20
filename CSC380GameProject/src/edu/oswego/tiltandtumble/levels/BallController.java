package edu.oswego.tiltandtumble.levels;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.oswego.tiltandtumble.worldObjects.Ball;


public class BallController extends ClickListener {

	enum MyKeys {
		LEFT, RIGHT, UP, DOWN
	}

	private static enum State {
		PAUSED,
		ACTIVE
	}


	private final Map<MyKeys, Boolean> keys = new EnumMap<MyKeys, Boolean>(MyKeys.class);
	private final boolean useAccelerometer;
	private Ball ball;

	private float tiltX = 0;
	private float tiltY = 0;
	private State currentState;
	private float keyX = 0;
	private float keyY = 0;
	private final float keyIncrement = 0.5f;

	public BallController(boolean useAccelerometer) {
		this.useAccelerometer = useAccelerometer;
		keys.put(MyKeys.LEFT, false);
		keys.put(MyKeys.RIGHT, false);
		keys.put(MyKeys.UP, false);
		keys.put(MyKeys.DOWN, false);
		currentState = State.ACTIVE;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public void update() {

		if(currentState == State.ACTIVE){
			if (useAccelerometer) {
				// accelerometer is reversed from screen coordinates, we are in landscape mode
				tiltX = Gdx.input.getAccelerometerY();
				tiltY = Gdx.input.getAccelerometerX();
			}
			else {
				// might as well accept either input
				updateFromDpad();
				updateFromKeys();

				tiltX = keyX;
				tiltY = keyY;
			}
			if (ball != null) {
				ball.applyLinearImpulse(tiltX* 0.001f, tiltY* -0.001f);

			}
		}
	}

	public void resetBall(){
		keyX = 0;
		tiltX = 0;
		keyY = 0;
		tiltY = 0;
	}

	public void pause(){
		currentState = State.PAUSED;
	}
	public void resume(){
		currentState = State.ACTIVE;
	}


	private void updateFromDpad() {
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
	}

	private void updateFromKeys() {
		if (Gdx.input.isKeyPressed(Keys.UP)) {
            decrementY();
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            incrementY();
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            decrementX();
        }
        else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            incrementX();
        }
	}
	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer,
			int button) {
		Gdx.app.log("touchDown", event.getListenerActor().getName());
		String name = event.getListenerActor().getName();
		if (name.equals("up")) {
			upPressed();
		}
		if (name.equals("down")) {
			downPressed();
		}
		if (name.equals("left")) {
			leftPressed();
		}
		if (name.equals("right")) {
			rightPressed();
		}
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer,
			int button) {
		Gdx.app.log("touchUp", event.getListenerActor().getName());
		String name = event.getListenerActor().getName();
		if (name.equals("up")) {
			upReleased();
		}
		if (name.equals("down")) {
			downReleased();
		}
		if (name.equals("left")) {
			leftReleased();
		}
		if (name.equals("right")) {
			rightReleased();
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
			keyX += keyIncrement;
		}
	}

	private void decrementX() {
		if (keyX <= -10) {
			keyX = -10;
		}
		else {
			keyX -= keyIncrement;
		}
	}

	private void incrementY() {
		if (keyY >= 10) {
			keyY = 10;
		}
		else {
			keyY += keyIncrement;
		}
	}

	private void decrementY() {
		if (keyY <= -10) {
			keyY = -10;
		}
		else {
			keyY -= keyIncrement;
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
