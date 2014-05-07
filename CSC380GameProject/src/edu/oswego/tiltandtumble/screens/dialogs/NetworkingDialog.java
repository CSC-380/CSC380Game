package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class NetworkingDialog extends Dialog {

	private final TiltAndTumble game;
	TextField initials = null;
	private final Sound button;
	
	public NetworkingDialog(String title, Skin skin, TiltAndTumble game){
		super(title, skin, "dialog");
		this.game = game;
		AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);
		
		padTop(50);
        setModal(true);
        setMovable(false);
        
        Table table = new Table(skin);
		table.setFillParent(true);
		//table.add("Multiplayer", "header").center();
		
		table.row();
		table.row().padTop(10);
		table.add("Initials:");
		table.row().padBottom(5);
		initials = new TextField("", skin);
		initials.setMaxLength(6);
		initials.setMessageText("AAAAAA");
		table.add(initials).width(80);

		button("Continue");
		getContentTable().add(table).center();

	}
	
	@Override
	protected void result(Object object) {
		super.result(object);
			button.play();
			String text = initials.getText();
			//do whatever here with the name
			
			game.showNetworkingLevelScreen(text);
		
	}

}
