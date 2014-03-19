package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.screens.GameScreen;


public class PauseDialog extends Dialog {

GameScreen screen;
	public PauseDialog(String title, Skin skin, GameScreen screen) {
		super(title, skin);
		this.screen = screen;
		text("\nPaused\n");
		button("Resume");
		
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		Gdx.app.log("dialog result", "" + object);
		screen.resume();
	}
}
