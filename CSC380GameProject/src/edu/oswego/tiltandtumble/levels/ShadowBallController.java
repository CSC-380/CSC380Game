package edu.oswego.tiltandtumble.levels;

import java.util.List;
import java.util.Map;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.net.JAVAClient;
import edu.oswego.tiltandtumble.net.JAVAServer;
import edu.oswego.tiltandtumble.worldObjects.ShadowBall;

public class ShadowBallController {
	private State currentState;
	private ShadowBall ball;
	private String name;
	
	private Session session;
	private Map<Integer, Float> pathX;
	private Map<Integer, Float> pathY;
	private int level;
	
	private int count = 0;
	private int blockNumber = 0;
	private JAVAServer server;
	private JAVAClient client;
	private boolean isServer;
	
	public ShadowBallController(Session session,String name,int level){
		//get info from server here store in something so update can use it		
		this.session = session;
		this.level = level+1;
		System.out.println("level " + level + "name" + name);
		ResultSet resultsx = session.execute("SELECT pathx FROM level"+level+" WHERE username = '"+name+"';");
		Row rowx = resultsx.one();
 	   	pathX = rowx.getMap("pathx", Integer.class, Float.class);
 	   	ResultSet resultsy = session.execute("SELECT pathy FROM level"+level+" WHERE username = '"+name+"';");
		Row rowy = resultsy.one();
	   	pathY = rowy.getMap("pathy", Integer.class, Float.class);
 	   	currentState = State.ACTIVE;
 	   	System.out.println("made shadow");
	}
	
	public ShadowBallController(Session session,String name, JAVAServer server){
		this.server = server;
		this.session = session;
		this.name = name;  	
		this.isServer = true;
	   	currentState = State.LIVE;
 	   	System.out.println("made live shadow");
	}
	
	public ShadowBallController(Session session,String name, JAVAClient client){
		this.client = client;
		this.session = session;
		this.name = name; 
		this.isServer = false;
	   	currentState = State.LIVE;
 	   	System.out.println("made live shadow");
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
			//System.out.println("shadow update");
//			
//				ResultSet resultsx = b.session.execute("SELECT pathx FROM "+b.name+" WHERE block = "+b.blockNumber+";");
//				Row rowx = resultsx.one();
//				//rowx.getFloat("pathx");
//				//b.pathX = rowx.getMap("pathx", Integer.class, Float.class);
//		 	   	
//		 	   	ResultSet resultsy = b.session.execute("SELECT pathy FROM "+b.name+" WHERE block = "+b.blockNumber+";");
//				Row rowy = resultsy.one();
//				//rowy.getFloat("pathy");
//			  // 	b.pathY = rowy.getMap("pathy", Integer.class, Float.class);
//				try{
//					b.ball.draw(delta, rowx.getFloat("pathx"), rowy.getFloat("pathy"));
//					//b.blockNumber++;
//				}catch(NullPointerException e){
//					try {
//						Thread.sleep((long) (0.5*delta));
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				}
//				b.blockNumber++;
				if(b.isServer){
					b.ball.draw(delta, b.server.getX(), b.server.getY());
				}else{
					b.ball.draw(delta, b.client.getX(), b.client.getY());
				}
				
			}
		};

		public void pause(ShadowBallController b) {}
		public void resume(ShadowBallController b) {}
		public void update(ShadowBallController b, float delta) {}
	}
}
