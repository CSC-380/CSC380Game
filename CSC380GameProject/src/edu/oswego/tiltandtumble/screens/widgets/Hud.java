package edu.oswego.tiltandtumble.screens.widgets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.screens.GameScreen;

public class Hud extends Window {
	private final Label levelDisplay;
	private final Label scoreDisplay;
	private final Label timerDisplay;
	private Sound buttonn;

	public Hud(final GameScreen screen, final Skin skin) {
		super("", skin, "hud");
		
		AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		buttonn = assetManager.get(musicFile, Sound.class);

		row().uniform().expandX();
		add("Level: ").right().padLeft(10);
		levelDisplay = new Label("", skin, "hud-values");
		add(levelDisplay).left();
		add("Score: ").right();
		scoreDisplay = new Label("", skin, "hud-values");
		add(scoreDisplay).left();
		add("Time: ").right();
		timerDisplay = new Label("", skin, "hud-values");
		add(timerDisplay).left();

		Image pauseImage = new Image(skin, "PauseButton2");
		add(pauseImage).right().fill(false).padRight(10);
		pauseImage.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonn.play();
				screen.togglePause();
				return true;
			}
		});
	}

	public void setLevel(String level) {
		levelDisplay.setText(level);
	}

	public void setLevel(int level) {
		setLevel(String.valueOf(level));
	}

	public void setScore(Score score) {
		scoreDisplay.setText(String.valueOf(score.getPoints()));
		timerDisplay.setText(score.getFormattedTime());
	}
}
