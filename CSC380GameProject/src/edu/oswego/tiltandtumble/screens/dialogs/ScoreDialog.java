package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
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

	public ScoreDialog(String title, Skin skin, TiltAndTumble game, GameScreen screen) {
		super(title, skin);
		this.game = game;
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
		Score total = new Score(0, 0);
		for (int i = 0; i < scores.size(); ++i) {
			Score s = scores.get(i);
			table.add(String.valueOf(i + 1)).left();
			table.add(s.getFormattedTime()).center();
			table.add(String.valueOf(s.getPoints())).right();
			total.setPoints(total.getPoints() + s.getPoints());
			total.setTime(total.getTime() + s.getTime());
			table.row();
		}
		table.add("Total:").right();
		if (screen.getCurrentLevel().isFailed()) {
			table.add(total.getFormattedTime()).center();
			table.add("0").right();
			table.row().padTop(10);
			table.add("You Failed!").colspan(3).center();
		}
		else {
			table.add(total.getFormattedTime()).center();
			table.add(String.valueOf(total.getPoints())).right();
			if (!screen.hasMoreLevels()) {
				table.row().padTop(10);
				table.add("Game Over!").colspan(3).center();
				if (game.getHighScores().isHighScore(total)) {
					table.row();
					table.add("New High Score!").colspan(3).center();
					table.row();
					table.add("Initials:");
					initials = new TextField("", skin);
					initials.setMaxLength(3);
					initials.setMessageText("AAA");
					table.add(initials).width(40);
					button("Save", total);
				}
			}
		}

		getContentTable().add(table).pad(5,5,5,5);

		button("Continue");
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		if (object instanceof Score) {
			String text = initials.getText();
			if (text != "") {
				game.getHighScores().add(new HighScore(text, (Score)object));
			}
			game.showPreviousScreen();
		}
		else {
			screen.loadNextLevel();
		}
	}
}
