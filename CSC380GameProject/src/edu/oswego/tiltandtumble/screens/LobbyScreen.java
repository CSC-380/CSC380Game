package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;

import edu.oswego.tiltandtumble.TiltAndTumble;



public class LobbyScreen extends AbstractScreen{
	Sound button;
	String userName;
	TiltAndTumble game;
	
	public LobbyScreen(TiltAndTumble game) {
		super(game);
		this.game = game;
	}

	@Override
	public void show() {
		InputMultiplexer multiplexer = new InputMultiplexer(stage,
				new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Keys.BACK){
					game.showPreviousScreen();
					return true;
				}
				return super.keyDown(keycode);
			}
		});
		userName = game.getName();
        Gdx.input.setInputProcessor(multiplexer);
        AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);
		Window table1 = new Window("\nOnline Multiplayer", skin);
		table1.setFillParent(true);
		table1.setModal(true);
		table1.setMovable(false);
		stage.addActor(table1);
        
		
		
		Table table = new Table(skin);
		ScrollPane pane = new ScrollPane(table, skin);
		table1.add(pane).expandY().padTop(40).padBottom(10);
        
        Table users = new Table(skin);
        ScrollPane namesDisplay = new ScrollPane(users, skin);
        table.add(namesDisplay).expandY().padTop(40).padBottom(10);
        namesDisplay.setSize(100, 100);
        users.add("Players in lobby", "header").center();
        users.row().padTop(10);
        users.add("1: " + userName);
        
        table.row();
        
        Table maps = new Table(skin);
        ScrollPane mapDisplay = new ScrollPane(maps, skin);
       // maps.setSize(50, 50);
        table.add(mapDisplay).expandY().padTop(40).padBottom(10);
        
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
//					game.showGameScreen(
//							new Integer(((TextButton)actor).getText().toString()) - 1,
//							GameScreen.Mode.LIVE);
				}
			});
		}
      
        

        
        
       table.row();
        
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(2);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
                game.showPreviousScreen();
            }
        });
		
	}

}
