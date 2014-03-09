package edu.oswego.tiltandtumble.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

import edu.oswego.tiltandtumble.worldObjects.Ball;

public class BallController implements InputProcessor {
    private final boolean useAccelerometer;
    private final Ball ball;

    private float tiltX = 0;
    private float tiltY = 0;

    public BallController(Ball ball, boolean useAccelerometer) {
        this.ball = ball;
        this.useAccelerometer = useAccelerometer;
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
        switch (keycode) {
        case Keys.LEFT:
            tiltX -= 0.001f;
            break;
        case Keys.RIGHT:
            tiltX += 0.001f;
            break;
        case Keys.UP:
            tiltY += 0.001f;
            break;
        case Keys.DOWN:
            tiltY -= 0.001f;
            break;
        case Keys.CENTER:
            tiltX = 0f;
            tiltY = 0f;
            break;

        default:
            break;
        }
        Gdx.app.log("keydown", tiltX + " " + tiltY);
        return false;
    }

    public void update() {
        if (useAccelerometer) {
            // accelerometer is reversed from screen coordinates, we are in landscape mode
            tiltX = Gdx.input.getAccelerometerY() * 0.001f;
            tiltY = Gdx.input.getAccelerometerX() * -0.001f;
        }
        ball.applyLinearImpulse(tiltX, tiltY);
    }

    public float getX() {
        return tiltX;
    }

    public float getY() {
        return tiltY;
    }
}
