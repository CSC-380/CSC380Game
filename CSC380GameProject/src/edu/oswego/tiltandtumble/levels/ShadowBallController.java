package edu.oswego.tiltandtumble.levels;

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
	private int level;
	private float x = 0;
	private float y = 0;
	
	private int count = 0;
	
	public ShadowBallController(Session session,String name,int level){
		//get info from server here store in something so update can use it		
		this.session = session;
		this.level = level+1;
		//System.out.println("level " + level + "name" + name);
		ResultSet resultsx = session.execute("SELECT pathx FROM level"+level+" WHERE username = '"+name+"';");
		Row rowx = resultsx.one();
 	   	pathX = rowx.getMap("pathx", Integer.class, Float.class);
 	   	ResultSet resultsy = session.execute("SELECT pathy FROM level"+level+" WHERE username = '"+name+"';");
		Row rowy = resultsy.one();
	   	pathY = rowy.getMap("pathy", Integer.class, Float.class);
 	   	currentState = State.ACTIVE;
 	   	//System.out.println("made shadow");
	}
	
	public ShadowBallController(String name){ 	
	   	currentState = State.LIVE;
 	   	//System.out.println("made live shadow");
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
	
	public void updateEnemyLocation(float x, float y){
		currentState.updateEnemyLocation(this, x, y);
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
				try{
				b.ball.draw(delta, b.pathX.get(b.count), b.pathY.get(b.count));

				b.count++;
				}catch(NullPointerException e){
					//do nothing
				}
			}
		},
		LIVE{
			@Override
			public void pause(ShadowBallController b) {
				b.changeState(PAUSED);
			}
			@Override
			public void update(ShadowBallController b, float delta) {
				b.ball.draw(delta, b.x, b.y);
				//System.out.println("x and y drawn");
			}
			
			public void updateEnemyLocation(ShadowBallController b, float x, float y){
				b.x = x;
				b.y = y;
			//	System.out.println("x and y updated");
			}
		};
		public void updateEnemyLocation(ShadowBallController b, float x, float y){}
		public void pause(ShadowBallController b) {}
		public void resume(ShadowBallController b) {}
		public void update(ShadowBallController b, float delta) {}
	}
}
