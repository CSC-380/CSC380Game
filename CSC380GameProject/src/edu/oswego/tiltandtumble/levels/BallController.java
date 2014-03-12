package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import edu.oswego.tiltandtumble.worldObjects.Ball;

// TODO: we may not need this to actually be an input processor.
public class BallController implements InputProcessor {
    private final boolean useAccelerometer;
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

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    public void update() {
        if (useAccelerometer) {
            // accelerometer is reversed from screen coordinates, we are in landscape mode
            tiltX = Gdx.input.getAccelerometerY() * 0.001f;
            tiltY = Gdx.input.getAccelerometerX() * -0.001f;
        }
        else {
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
}
