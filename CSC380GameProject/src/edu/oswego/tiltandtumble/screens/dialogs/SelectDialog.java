package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.MainScreen;


public class SelectDialog  extends Dialog {
	
	private final TiltAndTumble game;
	private final MainScreen screen;
	TextField initials = null;
	
	private static enum Buttons {
		ACCEPT,
		CREATE
	}
	
	public SelectDialog(String title, Skin skin, TiltAndTumble game, MainScreen screen) {
		super(title, skin, "dialog");
		this.game = game;
		this.screen = screen;
		
		padTop(50);
        setModal(true);
        setMovable(false);
        
        getButtonTable().row().uniform().fill();

		button("Accept a Challenge", Buttons.ACCEPT);
		button("Create a Challenge", Buttons.CREATE);
		
	}
	
	@Override
	protected void result(Object object) {
		super.result(object);
		if (object != null && object instanceof Buttons) {
			Buttons b = (Buttons)object;
			if (b == Buttons.ACCEPT) {
				game.setChallengeAcceptMode(true);
				game.setChallengeMode(false);
				game.showChallengeScreen();
			} else if (b == Buttons.CREATE) {
				game.setChallengeMode(true);
				game.setChallengeAcceptMode(false);
				game.showLevelScreen();
			}
		 else {
			screen.resume();
		}
	}
	}
}
