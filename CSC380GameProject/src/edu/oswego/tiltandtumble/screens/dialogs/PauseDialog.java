package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.GameScreen;


public class PauseDialog extends Dialog {
	private final GameScreen screen;
	private final TiltAndTumble game;

	private static enum Buttons {
		QUIT,
		SETTINGS,
		RETRY,
		CONTINUE
	}

	public PauseDialog(String title, Skin skin, GameScreen screen, TiltAndTumble game) {
		super(title, skin, "dialog");
		this.screen = screen;
		this.game = game;
		padTop(50);
        setModal(true);
        setMovable(false);

        getButtonTable().row().uniform().fill();
        button("Quit", Buttons.QUIT);
        getButtonTable().row().uniform().fill();
        button("Settings", Buttons.SETTINGS);
        getButtonTable().row().uniform().fill();
        button("Retry", Buttons.RETRY);
        getButtonTable().row().uniform().fill();
		button("Continue", Buttons.CONTINUE);

	}

	@Override
	protected void result(Object object) {
		super.result(object);
		Gdx.app.log("dialog result", "" + object);
		if (object != null && object instanceof Buttons) {
			Buttons b = (Buttons)object;
			if (b == Buttons.QUIT) {
				game.showPreviousScreen();
			} else if (b == Buttons.SETTINGS) {
				game.showSettingsScreen();
			} else if (b == Buttons.RETRY) {
				screen.loadLevel(screen.getCurrentLevel().getLevelNumber());
			} else if (b == Buttons.CONTINUE) {
				screen.resume();
			}
		} else {
			screen.resume();
		}
	}
}
