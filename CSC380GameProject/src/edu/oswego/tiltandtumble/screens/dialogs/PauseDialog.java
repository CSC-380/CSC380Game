package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.GameScreen;


public class PauseDialog extends Dialog {
	private final GameScreen screen;
	private final TiltAndTumble game;
	private final Sound button;
	private static enum Buttons {
		QUIT,
		SETTINGS,
		RETRY,
		CONTINUE
	}

	public PauseDialog(String title, Skin skin, GameScreen screen, final TiltAndTumble game) {
		super(title, skin, "dialog");
		this.screen = screen;
		this.game = game;
		padTop(50);
        setModal(true);
        setMovable(false);

        AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);

        getButtonTable().row().uniform().fill();
        button("Quit", Buttons.QUIT);
        getButtonTable().row().uniform().fill();
        button("Settings", Buttons.SETTINGS);
        getButtonTable().row().uniform().fill();
        button("Retry", Buttons.RETRY);
        getButtonTable().row().uniform().fill();
		button("Continue", Buttons.CONTINUE);
		this.addListener(new InputListener(){
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
		Gdx.app.log("dialog result", "" + object);
		if (object != null && object instanceof Buttons) {
			button.play();
			Buttons b = (Buttons)object;
			if (b == Buttons.QUIT) {
				game.playMusic();
				game.showPreviousScreen();
			} else if (b == Buttons.SETTINGS) {
				game.showSettingsScreen();
			} else if (b == Buttons.RETRY) {
				if (screen.getMode() == GameScreen.Mode.ARCADE) {
					screen.loadLevel(0);
				} else {
					screen.loadLevel(screen.getCurrentLevel().getLevelNumber());
				}
			} else if (b == Buttons.CONTINUE) {
				screen.resume();
			}
		} else {
			screen.resume();
		}
		
	}
}
