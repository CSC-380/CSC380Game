package edu.oswego.tiltandtumble.screens;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import appwarp.WarpController;
import appwarp.WarpListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.levels.AudioManager;
import edu.oswego.tiltandtumble.levels.BallController;
import edu.oswego.tiltandtumble.levels.DebugLevelRenderer;
import edu.oswego.tiltandtumble.levels.DefaultLevelRenderer;
import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.levels.LevelRenderer;
import edu.oswego.tiltandtumble.levels.ShadowBallController;
import edu.oswego.tiltandtumble.levels.WorldPopulator;
import edu.oswego.tiltandtumble.screens.dialogs.NetworkingScoreDialog;
import edu.oswego.tiltandtumble.screens.dialogs.PauseDialog;
import edu.oswego.tiltandtumble.screens.widgets.DPad;
import edu.oswego.tiltandtumble.screens.widgets.Hud;


public class MultiplayerGameScreen extends AbstractScreen implements WarpListener{

	private LobbyScreen prevScreen;

	private final BallController ballController;
	private final WorldPopulator worldPopulator;
	private Level level;
	private LevelRenderer renderer;
	private AudioManager audio;
	InputMultiplexer inputMux = new InputMultiplexer();

	boolean usingDpad = false;
	private final List<Score> scores = new ArrayList<Score>();
	private State currentState;

	private final Hud hud;

	private Dialog pauseDialog;
	
	private ShadowBallController shadowController;
	private int numLevel;

	public MultiplayerGameScreen(TiltAndTumble game, int currentLevel, LobbyScreen l) {
		super(game);
		this.numLevel = currentLevel +1;

		worldPopulator = new WorldPopulator(game.getAssetManager());
		this.prevScreen = l;
		
		//this is where to set multiple shadowballs
		shadowController = new ShadowBallController(game.getOpp());	
		ballController = new BallController(!game.getSettings().isUseDpad(), BallController.Mode.REALTIME, game.getName(),currentLevel+1);
		hud = new Hud(this, skin, game.getAssetManager());
		loadLevel(currentLevel);
		//hud.setScore(level.getScore());

	}
	

	public void loadLevel(int num) {
		changeState(State.GAME_READY);
			Gdx.app.log("GameScreen", "Loading level #" + num);
			if (level != null) {
				level.dispose();
				level = null;
			}
			if (renderer != null) {
				renderer.dispose();
				renderer = null;
			}
			if (audio != null) {
				game.getSettings().removeObserver(audio);
				audio.dispose();
				audio = null;
			}
			//Gdx.app.log("GameScreen", "Cleaned up previous level");
			level = new Level(num, game.getLevels().get(num), ballController, worldPopulator, game.getAssetManager(), shadowController);
			//Gdx.app.log("GameScreen", "Level loaded");
			renderer = new DefaultLevelRenderer(level,
					game.getWidth(), game.getHeight(),
					game.getSpriteBatch(),
					game.getAssetManager());
			if (game.getSettings().isDebugRender()) {
				renderer = new DebugLevelRenderer(renderer, ballController);
			}
			Gdx.app.log("GameScreen", "Renderer created");
			audio = new AudioManager(
					level,
					game.getSettings().isMusicOn(),
					game.getSettings().isSoundEffectOn(),
					game.getAssetManager());
			game.getSettings().addObserver(audio);
			Gdx.app.log("GameScreen", "Audio manager created");
			hud.setLevel(num + 1);
			//new Starter(this, skin, game).show(stage);
			Gdx.app.log("GameScreen", "Level starting...");
			WarpController.getInstance().setListener(this);
			start();
	}


	public Level getCurrentLevel() {
		return level;
	}


	public List<Score> getScores() {
		return scores;
	}


	@Override
	public void show() {
		Gdx.input.setInputProcessor(inputMux);
		inputMux.addProcessor(stage);
		inputMux.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.BACK){
					pause();
					return true;
				}
				return super.keyDown(keycode);
			}
		});

		if (game.getSettings().isUseDpad()){
			DPad dpad = new DPad(skin, ballController);
			dpad.setPosition(0, 0);
			stage.addActor(dpad);
		}
		hud.setPosition(0, stage.getHeight());
		hud.setHeight(32);
		hud.setWidth(stage.getWidth());
		stage.addActor(hud);

		currentState.show(this);
	}

	@Override
	protected void preStageRenderHook(float delta) {
		renderer.render(delta, game.getSpriteBatch(), game.getFont());
		currentState.render(this, delta);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (level != null) {
			level.dispose();
		}
		if (renderer != null) {
			renderer.dispose();
		}
		if (audio != null) {
			audio.dispose();
		}
	}
	

	public void togglePause() {
		currentState.togglePause(this);
	}

	@Override
	public void pause() {
		currentState.pause(this);
	}

	@Override
	public void resume() {
		currentState.resume(this);
	}

	public void start() {
		currentState.start(this);
	}

	private void changeState(State state) {
		currentState = state;
	}
	
	private void handleLeaveGame(){
		WarpController.getInstance().handleLeave();
	}

	@Override
	public void onWaitingStarted (String message) {
		
	}

	@Override
	public void onError (String message) {
		
	}

	@Override
	public void onGameStarted (String message) {
		
	}

	@Override
	public void onGameFinished (int code, boolean isRemote) {
		if(isRemote){
			prevScreen.onGameFinished(code, true);
			
		}else{
			//idk here
		}
		WarpController.getInstance().handleLeave();
	}

	@Override
	public void onGameUpdateReceived (String message) {
		//update received calculate time difference
		try {
			JSONObject data = new JSONObject(message);
			float x = (float)data.getDouble("x");
			float y = (float)data.getDouble("y");
			this.shadowController.updateEnemyLocation(x, y);
		} catch (Exception e) {
			// exception in onMoveNotificationReceived
		}
	}
	
	private static enum State{
		
		GAME_READY{
			public void start(MultiplayerGameScreen s) {
				s.ballController.resetBall();
				s.ballController.resume();
				s.shadowController.resume();
				s.level.start();
				s.audio.start();
				s.changeState(State.GAME_RUNNING);
			}
		},
		GAME_RUNNING{
			public void render(MultiplayerGameScreen s, float delta) {
				if (s.level.hasFinished() && s.level.isFailed()) {
					s.loadLevel(s.getCurrentLevel().getLevelNumber());
				}else if(s.level.hasFinished() &! s.level.isFailed()){
					s.currentState = GAME_OVER;
				}else{
					s.level.update(delta);
					s.hud.setTime(s.level.getScore());
				}
			}
			
			@Override
			public void pause(MultiplayerGameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
				s.ballController.pause();
				s.audio.pause();
				s.level.pause();
				s.changeState(State.GAME_PAUSED);
			}

			@Override
			public void togglePause(MultiplayerGameScreen s) {
				pause(s);
			}
			
		},
		GAME_PAUSED{
			@Override
			public void resume(MultiplayerGameScreen s) {
				// isVisible seems to always return true, not sure why...
				// make sure the dialog goes away, this can be called from the system
				// level rather than direct user interaction so we want to make sure
				// the game does not start playing before the window goes away.
				if (s.pauseDialog != null && s.pauseDialog.hasParent()) {
					s.pauseDialog.hide();
					s.pauseDialog = null;
				}
				s.ballController.resume();
				s.shadowController.resume();
				s.audio.start();
				s.level.resume();
				s.changeState(State.GAME_RUNNING);
			}

			@Override
			public void show(MultiplayerGameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
			}

			@Override
			public void togglePause(MultiplayerGameScreen s) {
				resume(s);
			}
			
			
		},
		GAME_OVER{
			public void render(MultiplayerGameScreen  s, float delta) {
				new NetworkingScoreDialog("", s.skin, s.game, s).show(s.stage);	
			}
		};
		
		public void start(MultiplayerGameScreen s) {}
		public void togglePause(MultiplayerGameScreen  s) {}
		public void pause(MultiplayerGameScreen  s) {}
		public void resume(MultiplayerGameScreen  s) {}
		public void show(MultiplayerGameScreen  s) {}
		public void render(MultiplayerGameScreen  s, float delta) {}
	}

	@Override
	public void onUserJoinedRoom(String user) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getNumPlayers() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
