package edu.oswego.tiltandtumble.worldObjects.tutorial;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Disposable;

import edu.oswego.tiltandtumble.collisionListener.BallCollisionListener;
import edu.oswego.tiltandtumble.screens.GameScreen;
import edu.oswego.tiltandtumble.worldObjects.Ball;

public abstract class TutorialObject implements BallCollisionListener, Disposable{
	
	private int type;
	public int pushBumper = 1;
	public int hole = 2;
	public int attractorForce = 3;
	public int switchT = 4;
	public int teleporter = 5;
	public int movingWall = 6;
	public int spike = 7;
	
	
	TutorialObject(int type) {
       this.type = type;
	}

	public int getType(){
		return type;
	}

	@Override
	public void handleBeginCollision(Contact contact, Ball ball) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEndCollision(Contact contact, Ball ball) {
		// TODO Auto-generated method stub
		
	}


}
