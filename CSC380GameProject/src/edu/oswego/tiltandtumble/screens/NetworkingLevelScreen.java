package edu.oswego.tiltandtumble.screens;

import appwarp.WarpController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class NetworkingLevelScreen extends AbstractScreen {

	Sound button;
	private boolean live;
	
	public NetworkingLevelScreen(TiltAndTumble game, boolean live) {
		super(game);
		this.live = live;
		
	}
	
	public void setLive(boolean live){
		this.live = live;
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
		
		AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);

		Window window;
		if(live){
			window = new Window("\nOnline Multiplayer", skin);
		}else{
		window = new Window("\nCreate A Challenge", skin);
		}
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);
        
        
        
        window.row().padTop(70).colspan(5);
		//window.row().pad(10, 10, 0, 10).width(75).center();
		window.add("Select a Level", "highlight");
		//window.row().pad(10, 10, 0, 10).width(75).center();
		window.row();
		
		int count = game.getLevels().size();
		int j =0;
		if(live){
			j = 2;
		}else{
			j =0;
		}
		window.row().pad(10);
		for (int i = j; i < count; i++) {
			
		
			if ((i % 5) == 0 &! live) {
				window.row().pad(10).width(75);
			}
			
			Button l = new TextButton(Integer.toString(i + 1), skin);
			window.add(l);
			l.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					if(!live){
					button.play();
					game.showGameScreen(
							new Integer(((TextButton)actor).getText().toString()) - 1,
							GameScreen.Mode.CREATE);
					}else{
						button.play();
						System.out.println("before");
						WarpController.getInstance().startApp(game.getName());
						System.out.println("after start");
						game.showLobbyScreen(new Integer(((TextButton)actor).getText().toString()) - 1);
						
					}
				}
			});
		}
		

		window.row().padTop(50).bottom().colspan(5).width(100);
		
		Button back = new TextButton("Go Back", skin);
		window.add(back).center();
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//game.setChallengeMode(false);
				button.play();
				game.showPreviousScreen();
			}
		});
	}
}
