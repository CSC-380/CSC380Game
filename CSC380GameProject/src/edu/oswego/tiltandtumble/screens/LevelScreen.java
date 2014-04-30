package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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

		Window window = new Window("\nLevels", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);

		window.row().padTop(25).colspan(5);
		window.add("Arcade Mode", "highlight");
		window.row().padTop(10).colspan(5).width(100);
		Button arcade = new TextButton("Play", skin);
		window.add(arcade);
		arcade.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(0, GameScreen.Mode.ARCADE);
			}
		});

		window.row().padTop(25).colspan(5);
		window.add("Practice Mode", "highlight");
		window.row().pad(10, 10, 0, 10).width(75);
		int count = game.getLevels().size();
		for (int i = 0; i < count; i++) {
			if ((i % 5) == 0) {
				window.row().pad(10).width(75);
			}
			Button l = new TextButton(Integer.toString(i + 1), skin);
			window.add(l);
			l.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.showGameScreen(
							new Integer(((TextButton)actor).getText().toString()) - 1,
							GameScreen.Mode.PRACTICE);
				}
			});
		}

		window.row().padBottom(10).padTop(50).bottom().colspan(5).width(100);
		Button back = new TextButton("Go Back", skin);
		window.add(back);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showPreviousScreen();
			}
		});
	}
}
