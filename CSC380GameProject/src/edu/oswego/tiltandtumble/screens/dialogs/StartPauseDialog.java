package edu.oswego.tiltandtumble.screens.dialogs;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.levels.Level;
import edu.oswego.tiltandtumble.screens.GameScreen;


public class StartPauseDialog extends Dialog {

GameScreen screen;
Level level;

	public StartPauseDialog(String title, Skin skin, GameScreen screen) {
		super(title, skin, "dialog");
		this.screen = screen;
		this.setClip(false);
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		Gdx.app.log("dialog result", "" + object);
		screen.resume();
	}
}
