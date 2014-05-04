package edu.oswego.tiltandtumble.levels;

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.worldObjects.ShadowBall;


public class ShadowBallController {
	private State currentState;
	private ShadowBall ball;
	
	private Session session;
	private Map<Integer, Float> pathX;
	private Map<Integer, Float> pathY;
	
	private int count = 0;
	
	public ShadowBallController(Session session){
<<<<<<< HEAD
		//get info from server here store in something so update can use it
		this.session = session;
		ResultSet results = session.execute("SELECT pathx FROM users WHERE username = 'schrecen';");
		Row row = results.one();
 	   	pathX = row.getMap("pathx", Integer.class, Float.class);
 	   	//USER name will be passed here
 	   	results = session.execute("SELECT pathy FROM users WHERE username = 'schrecen';");
 	   	row = results.one();
	   	pathY = row.getMap("pathy", Integer.class, Float.class);
=======
		//get info from server here store in something so update can use it		
		this.session = session;
		
		ResultSet resultsx = session.execute("SELECT pathx FROM users WHERE username = 'schrecen';");
		Row rowx = resultsx.one();
 	   	pathX = rowx.getMap("pathx", Integer.class, Float.class);
 	   	
 	   	ResultSet resultsy = session.execute("SELECT pathy FROM users WHERE username = 'schrecen';");
		Row rowy = resultsy.one();
	   	pathY = rowy.getMap("pathx", Integer.class, Float.class);
	   	
 	   	for(int i = 0; i < pathX.size(); ++i)
 	   		System.out.println(pathX.get(i));
>>>>>>> FETCH_HEAD
 	   	currentState = State.ACTIVE;
	}
	
	public void setBall(ShadowBall ball) {
		this.ball = ball;
	}
	
	public void update(float delta) {
		currentState.update(this, delta);
	}
	
	public void pause() {
		currentState.pause(this);
	}

	public void resume() {
		currentState.resume(this);
	}
	
	private void changeState(State state) {
		currentState = state;
	}
	
	private static enum State {
		PAUSED {
			@Override
			public void resume(ShadowBallController b) {
				b.changeState(ACTIVE);
			}
		},
		ACTIVE {
			@Override
			public void pause(ShadowBallController b) {
				b.changeState(PAUSED);
			}
			@Override
			public void update(ShadowBallController b, float delta) {
<<<<<<< HEAD
				b.ball.draw(delta, b.pathX.get(b.count), b.pathY.get(b.count));
=======
				b.ball.draw(delta,b.pathX.get(b.count),b.pathY.get(b.count));
>>>>>>> FETCH_HEAD
				b.count++;
			}
		};

		public void pause(ShadowBallController b) {}
		public void resume(ShadowBallController b) {}
		public void update(ShadowBallController b, float delta) {}
	}
}
