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
	private TextField initials = null;
	private final Sound button;
	private String title;
	
	public NetworkingDialog(String title, Skin skin, TiltAndTumble game){
		super(title, skin, "dialog");
		this.game = game;
		this.title = title;
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
		table.row().center();
		table.add("Initials:", "highlight").center();
		table.row().padBottom(5);
		initials = new TextField("", skin);
		initials.setMaxLength(6);
		initials.setMessageText("????");
		table.add(initials).width(80);

		button("Continue");
		getContentTable().add("Initials:").center();
		getContentTable().add(initials).center();
		//table.add("Initials:").center();

	}
	
	@Override
	protected void result(Object object) {
		super.result(object);
			button.play();
			String text = initials.getText();
			if(title.equalsIgnoreCase("Multiplayer")){
			game.showNetworkingLevelScreen(text);
			}else{
			game.showLobbyScreen(text);
			}
	}

}
