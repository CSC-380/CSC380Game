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
import edu.oswego.tiltandtumble.screens.LevelScreen.Mode;


public class ModeDialog extends Dialog{
	private final TiltAndTumble game;
	private final Sound button;
	private static enum Buttons {
		ARCADE,
		PRACTICE,
		TUTORIAL
	}
	
	public ModeDialog(String title, Skin skin, final TiltAndTumble game) {
		super(title, skin, "dialog");
		this.game = game;
		padTop(50);
        setModal(true);
        setMovable(false);

        AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);

		getButtonTable().row().uniform().fill();
		button("Tutorial", Buttons.TUTORIAL);
        getButtonTable().row().uniform().fill();
        button("Arcade Mode", Buttons.ARCADE);
        getButtonTable().row().uniform().fill();
        button("Single Map Mode", Buttons.PRACTICE);
        
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
		Gdx.app.log("dialog result", "" + object);
		if (object != null && object instanceof Buttons) {
			button.play();
			Buttons b = (Buttons)object;
			if (b == Buttons.ARCADE) {
				//game.showLevelScreen(Mode.ARCADE);
				game.showGameScreen(0, GameScreen.Mode.ARCADE);
			} else if (b == Buttons.PRACTICE) {
				game.showLevelScreen(Mode.SINGLE);
				
			} else if (b == Buttons.TUTORIAL) {
				game.showLevelScreen(Mode.TUTORIAL);
				
			}
		} else {
		

		}

	}

}
