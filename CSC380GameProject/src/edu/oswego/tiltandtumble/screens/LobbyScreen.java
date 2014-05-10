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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.dialogs.AddPlayerDialog;




public class LobbyScreen extends AbstractScreen{
	private Sound button;
	private String userName;
	private String opponent;
	private String lobby;
	private int numOfPlayers = 1;
	private int levelNum;
	private TiltAndTumble game;
	private Session session;	
	private State currentState;
	private ResultSet result ;
	private List<Row> row;	
	private Table users;
	private Dialog dialog;
	private Label badUserName;
	private Label privateLobby;
	
	public LobbyScreen(TiltAndTumble game) {
		super(game);
		this.game = game;
		session = game.getSession();
		dialog = new AddPlayerDialog("Add Player", skin, game, this);
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
		
		Window window = new Window("\nOnline Multiplayer", skin);
		window.setFillParent(true);
		window.setModal(true);
		window.setMovable(false);
        stage.addActor(window);
        privateLobby = new Label("Private Lobby", skin);
        window.add(privateLobby).padTop(40);
        privateLobby.setVisible(false);
		window.row().center().uniform().pad(10, 10, 0, 10);
			
		//session.execute("CREATE TABLE IF NOT EXISTS lobbyy (user ascii PRIMARY KEY, selected boolean, lobby ascii)");
		//session.execute("INSERT INTO lobbyy (user, selected, lobby) VALUES('"+userName+"', false, '"+userName+"');");
		//numOfPlayers++;
		session.execute("CREATE TABLE IF NOT EXISTS privateLobby"+userName+" (user ascii PRIMARY KEY)");
		session.execute("INSERT INTO privateLobby"+userName+" (user)VALUES ('"+userName+"');");
		lobby = userName;
//		session.execute("DELETE FROM lobby WHERE username = 'GOO'");
//		System.out.println("In waiting room");
//		session.execute("CREATE TABLE IF NOT EXISTS "+userName+"(block int PRIMARY KEY, pathx float,pathy float)");
//		session.execute("INSERT INTO "+userName+" (username) VALUES('"+userName +"',0,-1.0,-1.0);");
//		ResultSet lobby = session.execute("SELECT * FROM lobby");// WHERE '"+userName+"'");
//		List<Row> lobbyRow = lobby.all();
		
		users = new Table(skin);
		Table buttons = new Table(skin);
        users.row().center().uniform().pad(10,10,0,0);
        users.add("Players in lobby", "highlight");
        users.row().center().uniform();
        users.add("1: " + userName);

        window.add(users).left();
       
       
        
        Button addPlayers = new TextButton("Add Player", skin);
        Button startMatch = new TextButton ("Start Match", skin);
        buttons.row().center().uniform().pad(10,0,0,20);
        badUserName = new Label("Invalid UserName", skin);
		badUserName.setVisible(false);
		buttons.add(badUserName);
		buttons.row().uniform();
		buttons.add(addPlayers);
		window.add(buttons);
        
        addPlayers.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	
            	dialog.show(stage);
            		
            }
        });
        
        startMatch.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	changeState(State.STARTING);
            	
            }
        });
        
        
        
        window.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        window.add(back).bottom();
        window.add(startMatch).bottom();
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	session.execute("DELETE FROM lobbyy WHERE user = '"+userName+"'");
            	session.execute("DROP TABLE privateLobby"+userName+"");
                game.showPreviousScreen();
            }
        });
        
        changeState(State.WAITING);
		
	}
	
	public void addPlayer(String addedPlayer){
		ResultSet result = session.execute("SELECT * FROM privateLobby"+lobby+"");
		List<Row> row = result.all();
		//System.out.println("1 " + row.toString());
    	
		if(row.size() > 1){
			for(int i = 0; i< row.size();i++){
				String temp = row.get(i).getString("user");
				System.out.println("after dialog");
				if(temp.equals(addedPlayer)){
					numOfPlayers++;
					opponent = temp;
					users.row().padTop(10);
			        users.add(numOfPlayers+": "+ opponent);
			        game.setOpp(temp);
			        
			        privateLobby.setVisible(true);
			        badUserName.setVisible(false);
					return;
				}
			}
			
		}
		badUserName.setVisible(true);
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
					l.result = l.session.execute("SELECT * FROM lobbyy");
					l.row = l.result.all();
					//System.out.println(l.row.toString());
				
					for(int i = 0; i< l.row.size();i++){
						String temp = l.row.get(i).getString("user");
						if(temp.equals(l.userName)){
							if(l.row.get(i).getBool("selected")){
								System.out.println("selcected");
								l.lobby = l.row.get(i).getString("lobby");
								System.out.println(l.lobby);
								//l.session.execute("DROP TABLE privateLobby"+l.userName+"");
								l.result = l.session.execute("SELECT * FROM privateLobby"+l.lobby+"");
								l.row = l.result.all();
								System.out.println(l.row.size());
								for(int j = 0; j< l.row.size();j++){
									temp = l.row.get(j).getString("user");
									System.out.println(temp + "***" + l.userName);
									if(!temp.equals(l.userName)){
										l.opponent = temp;
										l.users.row().padTop(10);
										 l.numOfPlayers++;
								        l.users.add(l.numOfPlayers+": "+ l.opponent);
								        System.out.println("HERE");
								        l.game.setOpp(temp);
								        l.privateLobby.setVisible(true);
								      	
								    	l.session.execute("DELETE FROM lobbyy WHERE user = '"+l.userName+"'");
								        return;
									}
	
								}

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
			l.session.execute("DELETE FROM lobbyy WHERE user = '"+l.userName+"'");
			l.session.execute("DROP TABLE privateLobby"+l.userName+"");
			l.game.showGameScreen(l.levelNum, GameScreen.Mode.LIVE);
			
		}
		
		
	};

		public void show(LobbyScreen l) {}
		public void render(LobbyScreen l, float delta) {}
	}

}