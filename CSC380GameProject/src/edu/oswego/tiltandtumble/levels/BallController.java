package edu.oswego.tiltandtumble.levels;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import edu.oswego.tiltandtumble.worldObjects.*;

public class BallController {

	enum Keys {
		LEFT, RIGHT, UP, DOWN
	}

	//Gdx.input.getAccelerometerX();
	private WorldPopulator 	world;
	private Body 	ballBody;
	private Ball ball;
	
	
	
	static Map<Keys, Boolean> keys = new HashMap<BallController.Keys, Boolean>();
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
	};


	
	public BallController(WorldPopulator world) {
		this.world = world;
		this.ball = world.getBall();
		this.ballBody = world.getBallBody();
	}

	// ** Key presses and touches **************** //

	public void leftPressed() {
		keys.get(keys.put(Keys.LEFT, true));
	
	}

	public void rightPressed() {
		keys.get(keys.put(Keys.RIGHT, true));
	}

	public void upPressed() {
		keys.get(keys.put(Keys.UP, true));
	}
	public void downPressed(){
		keys.get(keys.put(Keys.DOWN, true));
	}


	public void leftReleased() {
		keys.get(keys.put(Keys.LEFT, false));
	}

	public void rightReleased() {
		keys.get(keys.put(Keys.RIGHT, false));
	}

	public void upReleased() {
		keys.get(keys.put(Keys.UP, false));
	}
	public void downReleased() {
		keys.get(keys.put(Keys.DOWN, false));
	}


	/** The main update method **/
	public void update(float delta) {
		processInput();
		ball.setPosition(ball.getPosition().add(ball.getVelocity().cpy().scl(delta)));
		ball.update(delta);
	}
	
	
	/** Change Ball's parameters based on input controls **/
	private void processInput() {
		if (keys.get(Keys.LEFT)) {
			ball.getVelocity().x = -Ball.SPEED;
		}
		if (keys.get(Keys.RIGHT)) {
			ball.getVelocity().x = Ball.SPEED;
		}
		if (keys.get(Keys.UP)) {
			ball.getVelocity().y = Ball.SPEED;
		}
		if (keys.get(Keys.DOWN)) {
			ball.getVelocity().y = -Ball.SPEED;
		}
		// need to check if both or none direction are pressed, 
		if ((keys.get(Keys.LEFT) && keys.get(Keys.RIGHT)) ||
				(!keys.get(Keys.LEFT) && !(keys.get(Keys.RIGHT)))) {
	
			// acceleration is 0 on the x
			ball.getAcceleration().x = 0;
			// horizontal speed is 0
			ball.getVelocity().x = 0;
		
	    }if ((keys.get(Keys.UP) && keys.get(Keys.DOWN)) ||
				(!keys.get(Keys.UP) && !(keys.get(Keys.DOWN)))) {
	
			// acceleration is 0 on the y
			ball.getAcceleration().y = 0;
			// vertical speed is 0
			ball.getVelocity().y = 0;
		}
	}
}
