package edu.oswego.tiltandtumble.screens;

import java.util.List;

import appwarp.WarpController;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class RenderThread implements Runnable {

	private boolean selected = false;
	private String userName;
	private String lobby;
	private Session session;
	private LobbyScreen l;
	
	//public RenderThread(Session session, String userName, String lobby) {
	public RenderThread(LobbyScreen l) {
		this.l= l;
		
	}
	
	public LobbyScreen getLobbyScreen() {
		
		return l;
	
	}
	
	public void run() {
		//System.out.println("in waiting render");
//		//System.out.println("CHECK 1");
//		for (;;) {
//			//System.out.println("CHECK 2");
//			l.result = l.session.execute("SELECT * FROM lobbyy");
//			l.row = l.result.all();
//			// System.out.println(l.row.toString());
//
//			for (int i = 0; i < l.row.size(); i++) {
//				String temp = l.row.get(i).getString("user");
//				if (temp.equals(l.userName)) {
//					boolean sel = l.row.get(i).getBool("selected");
//					if (sel) {
//						l.lobby = l.row.get(i).getString("lobby");
//						l.result = l.session
//								.execute("SELECT * FROM privateLobby" + l.lobby
//										+ "");
//						l.row = l.result.all();
//						for (int j = 0; j < l.row.size(); j++) {
//							temp = l.row.get(j).getString("user");
//							if (!temp.equals(l.userName)) {
//								l.opponent = temp;
//								l.users.row().padTop(10);
//								l.numOfPlayers++;
//								l.users.add(l.numOfPlayers + ": " + l.opponent);
//								l.game.setOpp(temp);
//								l.privateLobby.setVisible(true);
//								String roomId = l.row.get(j).getString("roomId");
//								WarpController.getInstance().onRoomCreated(roomId);
//
//								l.session
//										.execute("DELETE FROM lobbyy WHERE user = '"
//												+ l.userName + "'");
//								return;
//							}
//
//						}
//
//					}
//
//				}
//
//			}
//		}
	
	}
	
}
