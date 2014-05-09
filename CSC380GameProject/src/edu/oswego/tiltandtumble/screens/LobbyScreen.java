package edu.oswego.tiltandtumble.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;




public class LobbyScreen extends AbstractScreen{
	Sound button;
	String userName;
	String opponent;
	private int levelNum;
	TiltAndTumble game;
	Session session;	
	private State currentState;
	ResultSet result ;
	List<Row> row;	
	Window table;
	Table users;
	
	public LobbyScreen(TiltAndTumble game) {
		super(game);
		this.game = game;
		session = game.getSession();
	}

	@Override
	public void show() {
		
		//session.execute("CREATE TABLE IF NOT EXISTS lobby (username text PRIMARY KEY)");
//		session.execute("CREATE TABLE users (" + "username text PRIMARY KEY, "
//						+ "highscore int, " + "pathx map<int, float>, "
//						+ "pathy map<int, float>);");
		InputMultiplexer multiplexer = new InputMultiplexer(stage,
				new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.BACK){
					game.showPreviousScreen();
					return true;
				}
				return super.keyDown(keycode);
			}
		});
		
		levelNum = game.getLiveLevel();
		userName = game.getName();
        Gdx.input.setInputProcessor(multiplexer);
        
        AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);
		
		table = new Window("\nOnline Multiplayer", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        table.row().center().uniform().padTop(40);
		table.add("Name", "header");
		table.add("Time", "header");
		table.add("Initials", "header");
		table.add("Date", "header");
		table.row().center().uniform().padTop(5);
		
		
		
//		ResultSet r = session.execute("DESCRIBE KEYSPACE realtime;");
//		System.out.println(r.toString());
		
		
		session.execute("INSERT INTO lobby (username) VALUES('"+userName+"');");
	
		
		//session.execute("DELETE FROM lobby WHERE username = '9999'");
//		System.out.println("In waiting room");
//		session.execute("CREATE TABLE IF NOT EXISTS "+userName+"(block int PRIMARY KEY, pathx float,pathy float)");
//		session.execute("INSERT INTO "+userName+" (username) VALUES('"+userName +"',0,-1.0,-1.0);");
//		ResultSet lobby = session.execute("SELECT * FROM lobby");// WHERE '"+userName+"'");
//		List<Row> lobbyRow = lobby.all();
		
		users = new Table(skin);
        users.row().center().uniform();
        users.add("1: " + userName);
        table.add(users);
        
        
        
        table.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(4).bottom();
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	session.execute("DELETE FROM lobby WHERE username = '"+userName+"'");
                game.showPreviousScreen();
            }
        });
        
        changeState(State.WAITING);
		
	}
	
	
	private void changeState(State state) {
		currentState = state;
	}
	
	@Override
	protected void preStageRenderHook(float delta) {
		currentState.render(this, delta);
	}
	
	private static enum State {
		WAITING{
			
			public void show(LobbyScreen l){
				//display something to show the user stuff is happening
				
			}
			
			public void render(LobbyScreen l , float delta){
				//System.out.println("in waiting render");
					l.result = l.session.execute("SELECT * FROM lobby");
					l.row = l.result.all();
				//	System.out.println(l.row.toString());
				
				if(l.row.size() > 1){
					for(int i = 0; i< l.row.size();i++){
						String temp = l.row.get(i).getString("username");
						System.out.println(temp);
						if(!temp.equals(l.userName)){
							l.opponent = temp;
							l.users.row().padTop(10);
					        l.users.add("2: " + l.opponent);
					        l.game.setOpp(temp);
							l.changeState(STARTING);
							
						}
					}	
				}	
			}
	},
		STARTING{
		
		public void render(LobbyScreen l, float delta){
			System.out.println("Got to state starting");
			l.session.execute("CREATE TABLE IF NOT EXISTS "+l.userName+" (block int PRIMARY KEY, pathx float, pathy float)");
			l.session.execute("INSERT INTO "+l.userName+" (block, pathx, pathy)VALUES (0, -1.0, -1.0);");
			l.session.execute("DELETE FROM lobby WHERE username = '"+l.userName+"'");
			l.game.showGameScreen(l.levelNum, GameScreen.Mode.LIVE);
			
		}
		
		
	};

		public void show(LobbyScreen l) {}
		public void render(LobbyScreen l, float delta) {}
	}

}