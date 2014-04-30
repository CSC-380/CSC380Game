package edu.oswego.tiltandtumble.levels;

import edu.oswego.tiltandtumble.worldObjects.ShadowBall;


public class ShadowBallController {
	private State currentState;
	private ShadowBall ball;
	
	public ShadowBallController(){
		//get info from server here store in something so update can use it
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
			//	get new cords off map thing call .draw
			}
		};

		public void pause(ShadowBallController b) {}
		public void resume(ShadowBallController b) {}
		public void update(ShadowBallController b, float delta) {}
	}
}
