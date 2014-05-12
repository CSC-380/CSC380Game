package edu.oswego.tiltandtumble.screens;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import appwarp.WarpController;
import appwarp.WarpListener;

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




public class LobbyScreen extends AbstractScreen implements WarpListener{
	private Sound button;
	public String userName;
	public String opponent;
	public String lobby;
	public int numOfPlayers = 1;
	public int levelNum;
	public TiltAndTumble game;
	public Session session;	
	public State currentState;
	public ResultSet result ;
	public List<Row> row;	
	public Table users;
	public Dialog dialog;
	public Label badUserName;
	public Label privateLobby;
	private boolean first = true;
	private RenderThread rt;
	
	private final String[] tryingToConnect = {"Connecting","to AppWarp"};
	private final String[] waitForOtherUser = {"Waiting for","other user"};
	private final String[] errorInConnection = {"Error in","Connection", "Go Back"};
	
	private final String[] game_over = {"Congrats!", "Level Completed"};
	private final String[] enemy_left = {"Congrats You Win!", "Enemy Left the Game"};
	
	private String[] msg = tryingToConnect;

	public LobbyScreen(TiltAndTumble game) {
		super(game);
		this.game = game;
		session = game.getSession();
		dialog = new AddPlayerDialog("Add Player", skin, game, this);
	}

	@Override
	public void show() {
		

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
				//changeState(State.STARTING);
				WarpController.getInstance().startGame();
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
			//	session.execute("DELETE FROM lobbyy WHERE user = '"+userName+"'");
			//	session.execute("DROP TABLE privateLobby"+userName+"");
				WarpController.getInstance().handleLeave();
				game.showPreviousScreen();
			}
		});

		
		WarpController.getInstance().setListener(this);

		//session.execute("CREATE TABLE IF NOT EXISTS lobbyy (user ascii PRIMARY KEY, selected boolean, lobby ascii)");
		//session.execute("INSERT INTO lobbyy (user, selected, lobby) VALUES('"+userName+"', false, '"+userName+"');");
		//numOfPlayers++;
	//	session.execute("CREATE TABLE IF NOT EXISTS privateLobby"+userName+" (user ascii PRIMARY KEY, roomId ascii)");
	//	session.execute("INSERT INTO privateLobby"+userName+" (user, roomId)VALUES ('"+userName+"' , '"+WarpController.getInstance().getRoomID()+"');");
		lobby = userName;
		//		session.execute("DELETE FROM lobby WHERE username = 'GOO'");
		//		System.out.println("In waiting room");
		//		session.execute("CREATE TABLE IF NOT EXISTS "+userName+"(block int PRIMARY KEY, pathx float,pathy float)");
		//		session.execute("INSERT INTO "+userName+" (username) VALUES('"+userName +"',0,-1.0,-1.0);");
		//		ResultSet lobby = session.execute("SELECT * FROM lobby");// WHERE '"+userName+"'");
		//		List<Row> lobbyRow = lobby.all();
		changeState(State.WAITING);

	}
	
	@Override
	public void onError (String message) {
		this.msg = errorInConnection;
		
	}
	
	@Override
	public void onUserJoinedRoom(String user){
		numOfPlayers++;
		opponent = user;
		users.row().padTop(10);
		users.add(numOfPlayers+": "+ opponent);
		game.setOpp(user);

		privateLobby.setVisible(true);
		badUserName.setVisible(false);
	}
	
	@Override
	public int getNumPlayers(){
		return numOfPlayers;
	}

	@Override
	public void onGameStarted (String message) {
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run () {
				changeState(State.STARTING);
			}
		});
		
	}

	@Override
	public void onGameFinished (int code, boolean isRemote) {
		if(code==WarpController.GAME_OVER){
			this.msg = game_over;
		}else if(code==WarpController.ENEMY_LEFT){
			this.msg = enemy_left;
		}
		game.setScreen(this);
	}
	
	@Override
	public void onGameUpdateReceived (String message) {
		
	}

	@Override
	public void onWaitingStarted(String message) {
		this.msg = waitForOtherUser;

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

			public void render(LobbyScreen l , float delta){
				if(l.first) {
					new Thread(new RenderThread(l)).start();
					l.first = false;
				}

			}	


		},
		STARTING{
		
		public void render(LobbyScreen l, float delta){
			System.out.println("Got to state starting");
			//l.session.execute("DELETE FROM lobbyy WHERE user = '"+l.userName+"'");
			//l.session.execute("DROP TABLE privateLobby"+l.userName+"");
			l.game.showMultiplayerGameScreen(l.levelNum, l);
			
		}
		
		
	};

		public void render(LobbyScreen l, float delta) {}
	}


}