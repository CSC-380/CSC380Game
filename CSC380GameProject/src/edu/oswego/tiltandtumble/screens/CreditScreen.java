package edu.oswego.tiltandtumble.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;



public class CreditScreen extends AbstractScreen {

	Music button;
	public CreditScreen(final TiltAndTumble game){
		super(game);
	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);
        
        AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.wav";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Music.class);

		Window table = new Window("\nCredits", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);
        table.row();
        
        //More contents can be added to credits once the audio sources are ready
        
        table.add("Tilt and Tumble v1.0", "header").expandX();
        table.row();
        table.add("\nDesign and Develop:", "header");
        table.row();
        table.add("Bo Guan");
        table.row();
        table.add("Dylan Loomis");
        table.row();
        table.add("James Dejong");
        table.row();
        table.add("Kelly Maestri");
        table.row();
        table.add("Kevin Winahradsky");
        table.row();
        table.add();
        table.row();
        table.row();
        table.row();
        table.row();
        
        Button back = new TextButton("Go Back", skin);
        table.add(back);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
                game.showPreviousScreen();
            }
        });
	}
}
