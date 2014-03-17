package edu.oswego.tiltandtumble.levels;

import java.util.EnumMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import edu.oswego.tiltandtumble.worldObjects.Ball;
public class BallController extends ClickListener  {
	


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
	private int keyX = 0;
	private int keyY = 0;

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
				tiltX = Gdx.input.getAccelerometerY() * 0.001f;
				tiltY = Gdx.input.getAccelerometerX() * -0.001f;
			}
			else {
				// might as well accept either input
				updateFromDpad();
				updateFromKeys();

				tiltX = keyX * 0.001f;
				tiltY = keyY * -0.001f;
			}
			if (ball != null) {
				ball.applyLinearImpulse(tiltX, tiltY);
			}
		}
	}
	
	public void pauseBall(){
		currentState = State.PAUSED;
		ball.pauseBall();
	}
	public void resumeBall(){
		currentState = State.ACTIVE;
		ball.resumeBall();
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
		else if (name.equals("down")) {
			downPressed();
		}
		else if (name.equals("left")) {
			leftPressed();
		}
		else if (name.equals("right")) {
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
		else if (name.equals("down")) {
			downReleased();
		}
		else if (name.equals("left")) {
			leftReleased();
		}
		else if (name.equals("right")) {
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
