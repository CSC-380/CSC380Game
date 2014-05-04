package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class NetworkingLevelScreen extends AbstractScreen {

	Music button;
	
	public NetworkingLevelScreen(TiltAndTumble game) {
		super(game);
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
		Gdx.input.setInputProcessor(stage);
		
		AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Music.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Music.class);

		Window window = new Window("\nLevels", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);

		window.row().padTop(100).fillX();
		Button play = new TextButton("Click to Play Level One!", skin);
		window.add(play);
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(1, GameScreen.Mode.NETWORKING);
			}
		});

		window.row().padTop(10).fillX();
		Button play2 = new TextButton("Click to Play Level Two!", skin);
		window.add(play2);
		play2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(2, GameScreen.Mode.NETWORKING);
			}
		});

		window.row().padTop(10).fillX();
		Button play3 = new TextButton("Click to Play Level Three!", skin);
		window.add(play3);
		play3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(3,GameScreen.Mode.NETWORKING);
			}
		});

		window.row().padTop(50).bottom().fillX();
		Button back = new TextButton("Go Back", skin);
		window.add(back);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.setChallengeMode(false);
				game.showPreviousScreen();
			}
		});
	}
}
