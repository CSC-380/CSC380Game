package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.MainScreen;


public class SelectDialog  extends Dialog {
	
	private final TiltAndTumble game;
	private final MainScreen screen;
	Dialog getName;
	private Sound button;
	Skin skin;
	
	private static enum Buttons {
		ACCEPT,
		CREATE,
		LIVE
	}
	
	public SelectDialog(String title, Skin skin, TiltAndTumble game, MainScreen screen) {
		super(title, skin, "dialog");
		this.game = game;
		this.screen = screen;
		this.skin = skin;
		AssetManager assetManager = new AssetManager();
		String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);
		
		padTop(50);
        setModal(true);
        setMovable(false);
        
        getButtonTable().row().uniform().fill();

        button("Live play", Buttons.LIVE);
		getButtonTable().row();
		button("Accept a Challenge", Buttons.ACCEPT);
		getButtonTable().row();
		button("Create a Challenge", Buttons.CREATE);
		
	}
	
	@Override
	protected void result(Object object) {
		super.result(object);
		if (object != null && object instanceof Buttons) {
			button.play();
			Buttons b = (Buttons)object;
			if (b == Buttons.ACCEPT) {
				game.getSession().execute("USE challenges");
				game.showChallengeScreen();				
			} else if (b == Buttons.CREATE) {
				game.getSession().execute("USE challenges");
				getName = new NetworkingDialog("Multiplayer", skin, game, false).show(screen.getStage());				
			}else if(b == Buttons.LIVE){
				game.getSession().execute("USE realtime");
				//this prompts for the initials
				getName = new NetworkingDialog("Live", skin, game, false).show(screen.getStage());	
			}
		else {
			screen.resume();
		}
	}
	}
}
