package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class NetworkingDialog extends Dialog {

	private final TiltAndTumble game;
	private TextField initials = null;
	private final Sound button;
	private String title;
	private Skin skin;
	Label label;
	
	public NetworkingDialog(String title, Skin skin, TiltAndTumble game, boolean goodName){
		super(title, skin, "dialog");
		this.game = game;
		this.title = title;
		this.skin = skin;
		AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);
		
		padTop(50);
        setModal(true);
        setMovable(false);
        
        Table table = new Table(skin);
		table.setFillParent(true);
		//table.add("Multiplayer", "header").center();
		label = new Label("UserName already taken", skin);
		label.setVisible(goodName);
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
				game.showNetworkingLevelScreen(text, false);
			}else{
				//live
				
				Session session = game.getSession();
				ResultSet result = session.execute("SELECT * FROM lobbyy");
				List<Row> row = result.all();
				System.out.println(row.toString());
			
				for(int i = 0; i< row.size();i++){
					String temp = row.get(i).getString("user");
					if(temp.equals(text)){
						Dialog dialog = new NetworkingDialog(title, skin, game, true).show(game.getStage());
					}
				}	
				session.execute("INSERT INTO lobbyy (user, selected, lobby) VALUES('"+text+"', false, '"+text+"');");

				game.showNetworkingLevelScreen(text, true);
			}
	}

}
