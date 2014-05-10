package edu.oswego.tiltandtumble.screens.dialogs;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.LobbyScreen;

public class AddPlayerDialog extends Dialog {

	private final TiltAndTumble game;
	private TextField initials = null;
	private final Sound button;
	private String title;
	private Skin skin;
	private Session session;
	private String userName;
	private LobbyScreen screen;
	
	public AddPlayerDialog(String title, Skin skin, TiltAndTumble game, LobbyScreen screen){
		super(title, skin, "dialog");
		this.game = game;
		this.title = title;
		this.skin = skin;
		this.screen = screen;
		this.userName = game.getName();
		this.session = game.getSession();
		AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);
		
		padTop(50);
        setModal(true);
        setMovable(false);
        
        Table table = new Table(skin);
		table.setFillParent(true);
		

		
	
		table.row().padTop(10);
		table.row().center();
		table.add("Players Name:", "highlight").center();
		table.row().padBottom(5);
		initials = new TextField("", skin);
		initials.setMaxLength(6);
		initials.setMessageText("????");
		table.add(initials).width(80);

		button("Add");
		getContentTable().add("Initials:").center();
		getContentTable().add(initials).center();
		
		
		//table.add("Initials:").center();

	}
	
	
	@Override
	protected void result(Object object) {
		super.result(object);
			button.play();
			String text = initials.getText();
			
			ResultSet result = session.execute("SELECT * FROM lobbyy");
			List<Row> row = result.all();
			//System.out.println(row.toString());
		
		if(row.size() > 1){
			for(int i = 0; i< row.size();i++){
				String temp = row.get(i).getString("user");
				boolean selected = row.get(i).getBool("selected");
				if(temp.equals(text) &! selected){
					//System.out.println(temp);
					session.execute("INSERT INTO privateLobby"+userName+" (user)VALUES ('"+temp+"');");
					session.execute("UPDATE lobbyy SET selected = true WHERE user = '"+temp+"'");
					session.execute("UPDATE lobbyy SET lobby = '"+userName+"' WHERE user = '"+temp+"'");
					break;
					
				}
			}	
			
		}	
		screen.addPlayer(text);
	}

}
