package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class LevelScreen extends AbstractScreen {

	public LevelScreen(TiltAndTumble game) {
		super(game);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		Window window = new Window("\nLevels",skin);
        window.setFillParent(true);
        stage.addActor(window);
		Table table = new Table();
		table.setFillParent(true);
		window.addActor(table);

		Button play = new TextButton("Click to Play Level One!", skin);
		table.add(play);
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(1);
			}
		});

		table.row();
		Button back = new TextButton("Go Back", skin);
		table.add(back);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showPreviousScreen();
			}
		});
	}
}
