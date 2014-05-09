package edu.oswego.tiltandtumble.screens.dialogs;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class NetworkingDialog extends Dialog {

	private final TiltAndTumble game;
	private TextField initials = null;
	private final Sound button;
	private String title;
	private Skin skin;
	
	public NetworkingDialog(String title, Skin skin, TiltAndTumble game){
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
	
	private void showLevelSelect(final String userName){

//       
       Table maps = new Table(skin);
       ScrollPane mapDisplay = new ScrollPane(maps, skin);
      // maps.setSize(50, 50);
       getContentTable().add(mapDisplay).expandY().padTop(40).padBottom(10);
       
       maps.add("Vote for a Level", "highlight");
		//maps.row().pad(10, 10, 0, 10).width(75).center();
		maps.row();
		
		int count = game.getLevels().size();
		for (int i = 0; i < count; i++) {
			if ((i % 5) == 0) {
				maps.row().pad(10).width(75);
			}
			Button l = new TextButton(Integer.toString(i + 1), skin);
			maps.add(l);
			l.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					button.play();
					game.showLobbyScreen(userName, actor.getName());
				}
			});
		}
	}
	
	@Override
	protected void result(Object object) {
		super.result(object);
			button.play();
			String text = initials.getText();
			if(title.equalsIgnoreCase("Multiplayer")){
				game.showNetworkingLevelScreen(text);
			}else{
				this.showLevelSelect(text);
				//game.showLobbyScreen(text);
			}
	}

}
