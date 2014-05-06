package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.HighScore;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.screens.GameScreen;

public final class ScoreDialog extends Dialog {

	private final TiltAndTumble game;
	private final GameScreen screen;
	TextField initials = null;
	private final Sound button;

	public ScoreDialog(String title, Skin skin, final TiltAndTumble game, GameScreen screen) {
		super(title, skin, "dialog");
		this.game = game;
		this.screen = screen;

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
		table.add("Level #" + lastLevel + " Completed")
			.colspan(3).center();
		table.row().padTop(10).uniformX();

		table.add("Level", "header").left();
		table.add("Time", "header").center();
		table.add("Score", "header").right();
		table.row().padBottom(5).uniformX();
		Score total = new Score(0, 0);
		Table scoreTable = new Table(skin);
		ScrollPane scoresScroll = new ScrollPane(scoreTable, skin, "clear");
		table.add(scoresScroll).colspan(3).fillX().maxHeight(65);
		int firstLevel = lastLevel - scores.size();
		for (int i = 0; i < scores.size(); ++i) {
			scoreTable.row().uniformX().expandX();
			Score s = scores.get(i);
			scoreTable.add(String.valueOf(firstLevel + i + 1)).left();
			scoreTable.add(s.getFormattedTime()).center();
			scoreTable.add(String.valueOf(s.getPoints())).right();
			total.setPoints(total.getPoints() + s.getPoints());
			total.setTime(total.getTime() + s.getTime());
		}
		table.row().uniformX();
		table.add("Total:", "header").right();
		table.add(total.getFormattedTime()).center();
		table.add(String.valueOf(total.getPoints())).right();
		if (screen.getCurrentLevel().isFailed()) {
			table.row().padTop(10).uniformX();
			table.add("You Failed!", "highlight").colspan(3).center();
		} else {
			if (!screen.hasMoreLevels()) {
				table.row().padTop(10);
				table.add("Game Over!", "highlight").colspan(3).center();
			}
		}
		if (screen.getMode() == GameScreen.Mode.ARCADE
				&& (!screen.hasMoreLevels() || screen.getCurrentLevel().isFailed())
				&& game.getHighScores().isHighScore(total)) {
			table.row();
			table.add("New High Score!", "highlight").colspan(3).center();
			table.row();
			table.add("Initials:");
			initials = new TextField("", skin);
			initials.setMaxLength(3);
			initials.setMessageText("AAA");
			table.add(initials).width(60);

			button("Continue", total);
		} else {
			button("Continue");
		}

		getContentTable().add(table).pad(5,5,5,5);

		this.addListener(new InputListener(){
			@Override
			public boolean keyDown(InputEvent event, int keycode){
				 if(keycode == Keys.BACK){
				   	 game.showPreviousScreen();
				   	 return true;
				 }
				return false;
			}
		});
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		if (object instanceof Score) {
			String text = initials.getText();
			if (text != "") {
				game.getHighScores().add(new HighScore(text, (Score)object));
			}
			button.play();
			game.showPreviousScreen();
		} else {
			button.play();
			screen.loadNextLevel();
		}
	}
}
