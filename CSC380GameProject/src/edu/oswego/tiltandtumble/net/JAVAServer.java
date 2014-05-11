package edu.oswego.tiltandtumble.net;

import com.badlogic.gdx.ApplicationListener;

public class JAVAServer {	
	ServerMSG serverMSG;
	float pathx;
	float pathy;
	
	public static enum platformCode {DESKTOP, ANDROID, HTML5};
	
	public JAVAServer(platformCode pC)
	{
		super();
		
		serverMSG = new ServerMSG(8080, this, pC); //Don't use a port lower than 1024 on Android and Linux!
	}
	
	public void sendMessage(float x, float y){
		serverMSG.sendMessageToAll("POSITION -1 "+(int)x+" "+(int)y);
	}

	public void dispose() {

		serverMSG.close();
	}
	
	//Method called by the ServerMSG (bidirectional) when client moves the card
	public void changePosition(int PlayerId, int x, int y)
	{
		pathx = x;
		pathy = y;
	}
	
	public float getX(){
		return pathx;
	}
	
	public float getY(){
		return pathy;
	}
}


