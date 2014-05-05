package edu.oswego.tiltandtumble.screens.widgets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.GameScreen;

public class Starter extends Dialog {
	private static final float MAX_COUNT = 4;

	private final GameScreen screen;
	private final Skin skin;
	private State currentState;
	private float countdownTime;
	private int lastCount;
	private Music zero;
	private Music one;
	private Music two;
	private Music three;
	private Sound buttonSound;
	private AssetManager assetManager;
	private TiltAndTumble game;

	public Starter(GameScreen screen, Skin skin, TiltAndTumble game) {
		super("", skin, "countdown");
		this.screen = screen;
		this.game = game;
		this.skin = skin;
		assetManager = new AssetManager();
		setFillParent(true);
        setModal(true);
        setMovable(false);
		String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		buttonSound = assetManager.get(musicFile, Sound.class);
        musicFile = "data/soundfx/number-zero.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		zero = assetManager.get(musicFile, Music.class);
        musicFile = "data/soundfx/number-one.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		one = assetManager.get(musicFile, Music.class);
		musicFile = "data/soundfx/number-two.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		two = assetManager.get(musicFile, Music.class);
		musicFile = "data/soundfx/number-three.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		three = assetManager.get(musicFile, Music.class);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		currentState.act(this, delta);
	}

	@Override
	public Dialog show(Stage stage) {
        currentState = State.WAITING;
        countdownTime = 0;
        lastCount = 0;

		final TextButton button = new TextButton("Start", skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.endMusic();
				buttonSound.play();
				button.remove();
				currentState = State.COUNTING;
			}
		});
		getContentTable().add(button);

		return super.show(stage);
	}

	private static enum State {
		COUNTING {
			@Override
			public void act(Starter s, float delta) {
				s.countdownTime += delta;
				if (s.countdownTime > MAX_COUNT) {
					s.hide();
					s.screen.start();
					return;
				}

				int count = (int)Math.floor(MAX_COUNT - s.countdownTime);
				if (s.lastCount != count) {
					s.clear();
					Label text = new Label(String.valueOf(count), s.skin, "countdown");
					if(count == 0){
						s.zero.play();
					}else if(count == 1){
						s.one.play();
					}else if(count == 2){
						s.two.play();
					}else if(count == 3){
						s.three.play();
					}
					s.add(text);
					s.pack();
				}
				s.lastCount = count;
			}
		},
		WAITING;

		public void act(Starter s, float delta) {}
	}
}
