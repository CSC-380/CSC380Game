package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.Score;
import edu.oswego.tiltandtumble.screens.GameScreen;

public final class ScoreDialog extends Dialog {

	private final GameScreen screen;

	public ScoreDialog(String title, Skin skin, TiltAndTumble game, GameScreen screen) {
		super(title, skin);
		this.screen = screen;

		padTop(25);
        setModal(true);
        setMovable(false);

        List<Score> scores = screen.getScores();

		Table table = new Table(skin);
		table.setFillParent(true);
		table.add("Level #" + scores.size() + " Completed")
			.colspan(3).center();
		table.row().padTop(10);

		table.add("Level");
		table.add("Time");
		table.add("Score");
		table.row().padBottom(5);
		int total = 0;
		for (int i = 0; i < scores.size(); ++i) {
			Score s = scores.get(i);
			table.add(String.valueOf(i + 1)).left();
			table.add(s.getFormattedTime()).center();
			table.add(String.valueOf(s.getPoints())).right();
			total += s.getPoints();
			table.row();
		}
		table.add("Total:").colspan(2).right();
		table.add(String.valueOf(total)).right();

		if (!screen.hasMoreLevels()) {
			if (game.getHighScores().isHighScore(scores.get(scores.size() - 1))) {
		        // TODO: add some sort of form element for adding the high score!
			}
		}
		getContentTable().add(table);

		button("Continue");
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		Gdx.app.log("dialog result", "" + object);
		screen.loadNextLevel();
	}
}
