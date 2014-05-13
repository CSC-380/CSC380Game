package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.screens.GameScreen;
import edu.oswego.tiltandtumble.screens.MultiplayerGameScreen;

public class NetworkingScoreDialog extends Dialog {

	private final TiltAndTumble game;
	private final GameScreen screen;
	private final MultiplayerGameScreen Mscreen;
	private final Sound button;
	
	public NetworkingScoreDialog(String title, Skin skin, TiltAndTumble game, GameScreen screen) {
		super(title, skin, "dialog");
		this.game = game;
		this.screen = screen;
		this.Mscreen = null;

		AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);

		padTop(50);
        setModal(true);
        setMovable(false);

        List<Score> scores = screen.getScores();

		Table table = new Table(skin);
		table.setFillParent(true);
		int lastLevel = screen.getCurrentLevel().getLevelNumber() + 1;
		table.add("Level #" + lastLevel + " Completed").colspan(3).center();
		table.row().padTop(10).uniformX();

		table.add("Level", "header").left();
		table.add("Time", "header").center();
		table.add("Score", "header").right();
		table.row().padBottom(5).uniformX();
		Score total = new Score(0, 0);
		int firstLevel = lastLevel - scores.size();
		for (int i = 0; i < scores.size(); ++i) {
			Score s = scores.get(i);
			table.add(String.valueOf(firstLevel + i + 1)).left();
			table.add(s.getFormattedTime()).center();
			table.add(String.valueOf(s.getPoints())).right();
			total.setPoints(total.getPoints() + s.getPoints());
			total.setTime(total.getTime() + s.getTime());
			table.row().uniformX();
		}
		//table.add("Total:", "header").right();
		//table.add(total.getFormattedTime()).center();
		//table.add(String.valueOf(total.getPoints())).right();
		if (screen.getCurrentLevel().isFailed()) {
			table.row().padTop(10).uniformX();
			table.add("You Failed!", "highlight").colspan(3).center();
		} else {
			if (!screen.hasMoreLevels()) {
				table.row().padTop(10);
				table.add("Game Over!", "highlight").colspan(3).center();
			}
		}
		button("Continue");

		getContentTable().add(table).pad(5,5,5,5);
	}
	
	public NetworkingScoreDialog(String title, Skin skin, TiltAndTumble game, MultiplayerGameScreen screen) {
		super(title, skin, "dialog");
		this.game = game;
		this.Mscreen = screen;
		this.screen = null;

		AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);

		padTop(50);
        setModal(true);
        setMovable(false);

        List<Score> scores = screen.getScores();

		Table table = new Table(skin);
		table.setFillParent(true);
		int lastLevel = screen.getCurrentLevel().getLevelNumber() + 1;
		table.add("Level #" + lastLevel + " Completed").colspan(3).center();
		table.row().padTop(10).uniformX();

		table.add("Level", "header").left();
		table.add("Time", "header").center();
		table.add("Score", "header").right();
		table.row().padBottom(5).uniformX();
		Score total = new Score(0, 0);
		int firstLevel = lastLevel - scores.size();
		for (int i = 0; i < scores.size(); ++i) {
			Score s = scores.get(i);
			table.add(String.valueOf(firstLevel + i + 1)).left();
			table.add(s.getFormattedTime()).center();
			table.add(String.valueOf(s.getPoints())).right();
			total.setPoints(total.getPoints() + s.getPoints());
			total.setTime(total.getTime() + s.getTime());
			table.row().uniformX();
		}
		//table.add("Total:", "header").right();
		//table.add(total.getFormattedTime()).center();
		//table.add(String.valueOf(total.getPoints())).right();

			table.row().padTop(10);
			table.add("Somebody won might have been you!", "highlight").colspan(3).center();
			
		
		button("Continue");

		getContentTable().add(table).pad(5,5,5,5);
	}

	@Override
	protected void result(Object object) {
		super.result(object);
			button.play();
			game.showPreviousScreen();
		
	}
}
