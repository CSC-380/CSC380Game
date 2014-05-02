package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.SettingsScreen.InputAdapter;

public class LevelScreen extends AbstractScreen {

	public LevelScreen(TiltAndTumble game) {
		super(game);
	}

	@Override
	public void show() {
		InputAdapter mProcessor = new InputAdapter();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);

		Window window = new Window("\nLevels", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);

		window.row().padTop(65).fillX();
		Button play = new TextButton("Click to Play Level One!", skin);
		window.add(play);
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(1);
			}
		});

		window.row().padTop(10).fillX();
		Button play2 = new TextButton("Click to Play Level Two!", skin);
		window.add(play2);
		play2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(2);
			}
		});

		window.row().padTop(10).fillX();
		Button play3 = new TextButton("Click to Play Level Three!", skin);
		window.add(play3);
		play3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(3);
			}
		});
		
		window.row().padTop(10).fillX();
		Button play4 = new TextButton("Click to Play Level Four!", skin);
		window.add(play4);
		play4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showGameScreen(4);
			}
		});

		window.row().padTop(50).bottom().fillX();
		Button back = new TextButton("Go Back", skin);
		window.add(back);
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.showPreviousScreen();
			}
		});
	}
	public class InputAdapter implements InputProcessor{

   	 public boolean keyDown(int keycode){
   		 if(keycode == Keys.BACK){
					game.showPreviousScreen();
					return true;
   		 }
   		 return false;
   	 }

		public boolean keyUp(int keycode) {
			return false;
		}

		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean touchDown(int screenX, int screenY, int pointer,int button) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
   	}
}
