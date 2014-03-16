package edu.oswego.tiltandtumble.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class ScoreDialog extends Dialog {

	private final GameScreen screen;

	public ScoreDialog(String title, Skin skin, GameScreen screen) {
		super(title, skin);
		this.screen = screen;
		text("Score: TODO");
		button("Continue");
	}

	@Override
	protected void result(Object object) {
		super.result(object);
		Gdx.app.log("dialog result", "" + object);
		screen.loadNextLevel();
	}
}
