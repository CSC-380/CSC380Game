package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class LevelScreen extends AbstractScreen {
Sound button;
private Mode currentMode;
public static enum Mode {
	TUTORIAL, SINGLE
}

	public LevelScreen(TiltAndTumble game, Mode mode) {
		super(game);
		this.currentMode = mode;
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
        Gdx.input.setInputProcessor(multiplexer);

        AssetManager assetManager = game.getAssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		button = assetManager.get(musicFile, Sound.class);

		Window window = new Window("\nPick a Level", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);
        
//        if(currentMode == Mode.ARCADE){
//        	
//        
//		window.row().padTop(20).colspan(5);
//		window.add("Arcade Mode", "highlight");
//		window.row().padTop(15).colspan(5).width(100);
//		Button arcade = new TextButton("Play", skin);
//		window.add(arcade);
//		arcade.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				game.showGameScreen(0, GameScreen.Mode.ARCADE);
//			}
//		});
       if(currentMode == Mode.TUTORIAL){
    	    Table table2 = new Table(skin);
   			ScrollPane scroll = new ScrollPane(table2, skin);
   			
   		
        	window.row().padTop(35).colspan(5);
    		window.add("Tutorial Mode", "highlight");
    		window.row().padTop(15).colspan(5);
    		window.add(scroll).expandY().padTop(40).padBottom(10);
    		int count = game.getTutorials().size();
    		for (int i = 0; i < count; i++) {
    			if ((i % 4) == 0) {
    				table2.row().pad(10, 10, 0, 10).width(75);
    			}
    			Button l = new TextButton(Integer.toString(i + 1), skin);
    			table2.add(l);
    			l.addListener(new ChangeListener() {
    				@Override
    				public void changed(ChangeEvent event, Actor actor) {
    					button.play();
    					game.showGameScreen(
    							new Integer(((TextButton)actor).getText().toString()) - 1,
    							GameScreen.Mode.PRACTICE);
    							
    				}
    			});
    	    	}
        	
        }
        else{
		window.row().padTop(35).colspan(5);
		window.add("Select A Level", "highlight");
		window.row().padTop(15).colspan(5);
		Table table2 = new Table(skin);
		ScrollPane scroll = new ScrollPane(table2, skin);
		window.add(scroll).expandY().padTop(40).padBottom(10);
		int count = game.getLevels().size();
		for (int i = 0; i < count; i++) {
			if ((i % 4) == 0) {
				table2.row().pad(10, 10, 0, 10).width(75);
			}
			Button l = new TextButton(Integer.toString(i + 1), skin);
			table2.add(l);
			l.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					button.play();
					game.showGameScreen(
							new Integer(((TextButton)actor).getText().toString()) - 1,
							GameScreen.Mode.PRACTICE);
				}
			});
	    	}
        }
		window.row().expand().padBottom(10);
		Button back = new TextButton("Go Back", skin);
		window.add(back).bottom();
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				button.play();
				game.showPreviousScreen();
			}
		});
	}
}
