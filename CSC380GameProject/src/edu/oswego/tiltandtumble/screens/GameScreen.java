package edu.oswego.tiltandtumble.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

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
import edu.oswego.tiltandtumble.screens.dialogs.ScoreDialog;
import edu.oswego.tiltandtumble.screens.widgets.DPad;
import edu.oswego.tiltandtumble.screens.widgets.Hud;
import edu.oswego.tiltandtumble.screens.widgets.Starter;


public class GameScreen extends AbstractScreen {
	public static enum Mode {
		ARCADE, 
		PRACTICE ,  
		ACCEPT, 
		CREATE, 
		LIVE
	}

	private final BallController ballController;
	private final WorldPopulator worldPopulator;

	private final Mode currentMode;
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
	private String name;
	Session session;
	private int numLevel;

	public GameScreen(TiltAndTumble game, int currentLevel, Mode mode) {
		super(game);
		this.numLevel = currentLevel +1;

		worldPopulator = new WorldPopulator(game.getAssetManager());

		if(mode == Mode.ACCEPT){
			session = game.getSession();
			//shadow only
			name = game.getName();
			System.out.println("ghost ball name " +name);
			shadowController = new ShadowBallController(game.getSession(),name,currentLevel+1);
			ballController = new BallController(!game.getSettings().isUseDpad(), BallController.Mode.NORMAL);

		}else if(mode == Mode.CREATE){
			//writing path only
			session = game.getSession();
			name = game.getName();
			name = game.getName();
			ballController = new BallController(!game.getSettings().isUseDpad(), BallController.Mode.WRITE, game.getSession(), name,currentLevel+1);
		}else if(mode == Mode.LIVE){
			//need a shadowball and to write
			session = game.getSession();
			name = game.getName();
			shadowController = new ShadowBallController(game.getSession(),name);	
			ballController = new BallController(!game.getSettings().isUseDpad(), BallController.Mode.REALTIME, game.getSession(), name,currentLevel+1);
		}else{
			ballController = new BallController(!game.getSettings().isUseDpad(), BallController.Mode.NORMAL);
		}


		hud = new Hud(this, skin, game.getAssetManager());
		loadLevel(currentLevel);
		hud.setScore(level.getScore());
		currentMode = mode;
	}
	

	public void loadLevel(int num) {
		changeState(State.WAITING);
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
		level = new Level(num, game.getLevels().get(num), ballController, worldPopulator, game.getAssetManager(),shadowController);
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
		new Starter(this, skin, game).show(stage);
		Gdx.app.log("GameScreen", "Level starting...");
	}

	public boolean hasMoreLevels() {
		if (currentMode == Mode.ARCADE){
			return level.getLevelNumber() < game.getLevels().size();
		}
		return false;
	}

	public void loadNextLevel() {
		if (hasMoreLevels() && !level.isFailed()) {
			loadLevel(level.getLevelNumber() + 1);
		}
		else {
			game.showPreviousScreen();
		}
	}

	public Level getCurrentLevel() {
		return level;
	}


	public List<Score> getScores() {
		return scores;
	}

	public Mode getMode() {
		return currentMode;
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

	private static enum State {
		WAITING {
			@Override
			public void show(GameScreen s) {
				new Starter(s, s.skin, s.game).show(s.stage);
			}

			@Override
			public void start(GameScreen s) {
				s.ballController.resetBall();
				
				s.ballController.resume();
				//shadowball.start
				if(s.shadowController != null){
				s.shadowController.resume();
				}
				s.level.start();
				s.audio.start();
				s.changeState(State.PLAYING);
			}
		},
		PAUSED {
			@Override
			public void resume(GameScreen s) {
				// isVisible seems to always return true, not sure why...
				// make sure the dialog goes away, this can be called from the system
				// level rather than direct user interaction so we want to make sure
				// the game does not start playing before the window goes away.
				if (s.pauseDialog != null && s.pauseDialog.hasParent()) {
					s.pauseDialog.hide();
					s.pauseDialog = null;
				}
				s.ballController.resume();
				if(s.shadowController != null){
				s.shadowController.resume();
				}
				s.audio.start();
				s.level.resume();
				s.changeState(State.PLAYING);
			}

			@Override
			public void show(GameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
			}

			@Override
			public void togglePause(GameScreen s) {
				resume(s);
			}
		},
		PLAYING {
			@Override
			public void render(GameScreen s, float delta) {
				if (s.level.hasFinished()) {
					s.audio.pause();
					
					if(s.getMode() != GameScreen.Mode.CREATE && s.getMode() != GameScreen.Mode.ACCEPT){
						//normal game play
						s.scores.add(s.level.getScore());
						new ScoreDialog("Score", s.skin, s.game, s).show(s.stage);
						
					}else{
						// create or accept or live game play
						s.scores.add(s.level.getScore());
						new NetworkingScoreDialog("", s.skin, s.game, s).show(s.stage);
						System.out.println("highscores");

						if(s.getMode() == GameScreen.Mode.CREATE){
							//create game play
							System.out.println("name " +s.name +" level "+s.numLevel + " score "+s.level.getScore().getPoints());
							s.session.execute("UPDATE level"+s.numLevel+" SET highscore = "+ s.level.getScore().getPoints()+" WHERE username = '"+s.name+"'");
													
							System.out.println(s.level.getLevelNumber());
							com.datastax.driver.core.ResultSet result = s.session.execute("SELECT username,highscore FROM level"+s.numLevel);
							List<Row> lRow = result.all();
							ArrayList<Row> sortLRow = new ArrayList<Row>();
							if(lRow.size()>6){
								for(Row r:lRow){
									if(sortLRow.size()>0){
										for(int i = 0; i < sortLRow.size(); i++){
											if(r.getInt("highscore")>sortLRow.get(i).getInt("highscore")){
												sortLRow.add(i,r);
												break;
											}else if(i == sortLRow.size()-1){
												sortLRow.add(r);
												break;
											}
										}
									}else{
										sortLRow.add(r);
									}								
								}
								String temp = sortLRow.get(sortLRow.size()-1).getString("username");
								s.session.execute("Delete From level"+s.numLevel+" WHERE username = '"+temp+"';");
								
							}

						}

					}
					
					s.changeState(State.SCORED);
				}
				else {
					s.level.update(delta);
				}

				s.hud.setScore(s.level.getScore());
			}

			@Override
			public void pause(GameScreen s) {
				s.pauseDialog = new PauseDialog("Paused", s.skin, s, s.game).show(s.stage);
				s.ballController.pause();
				s.audio.pause();
				s.level.pause();
				s.changeState(State.PAUSED);
			}

			@Override
			public void togglePause(GameScreen s) {
				pause(s);
			}
		},
		SCORED;

		public void start(GameScreen s) {}
		public void togglePause(GameScreen s) {}
		public void pause(GameScreen s) {}
		public void resume(GameScreen s) {}
		public void show(GameScreen s) {}
		public void render(GameScreen s, float delta) {}
	}
}
