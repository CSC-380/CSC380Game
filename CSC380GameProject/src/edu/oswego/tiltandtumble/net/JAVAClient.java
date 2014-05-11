package edu.oswego.tiltandtumble.net;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector3;


public class JAVAClient {

	
	float pathx;
	float pathy;
	ClientMSG clientMSG;
	int myID;
	public static enum platformCode {DESKTOP, ANDROID, HTML5};
	
	public JAVAClient(platformCode pC)
	{
		super();
		
		//Here we must create the client connection to the server
		clientMSG = new ClientMSG("127.0.0.1", 8080, this, pC); //Change here the IP and Port for your Server IP and Port
		myID = clientMSG.getId();
	}
	
	

	public void dispose() {
		clientMSG.close();
	}


	public void sendMessage(float x, float y){
		clientMSG.sendMessage("POSITION "+clientMSG.getId()+" "+(int)x+" "+(int)y);
	}

	/*
	* Method called by the ClientMSG (bidirectional) when server moves the card
	*/
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

