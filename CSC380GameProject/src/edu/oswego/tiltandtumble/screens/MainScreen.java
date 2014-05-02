package edu.oswego.tiltandtumble.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;


public class MainScreen extends AbstractScreen {

    public MainScreen(final TiltAndTumble game) {
        super(game);
    }

    @Override
    public void show() {
    	Gdx.input.setCatchBackKey(true);
    	InputAdapter mProcessor = new InputAdapter();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        
        Window window = new Window("\nTilt and Tumble", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);
        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        window.addActor(table);

        Button play = new TextButton("Play", skin);
        table.add(play).width(100).pad(10).padBottom(20).colspan(4);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showLevelScreen();
            }
        });

        table.row().pad(10).padBottom(20).uniform().fill().bottom();
        Button settings = new TextButton("Settings", skin);
        table.add(settings);
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showSettingsScreen();
            }
        });

        Button scores = new TextButton("Scores", skin);
        table.add(scores);
        scores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showHighScoresScreen();
            }
        });

        Button help = new TextButton("Help", skin);
        table.add(help);
        help.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showHelpScreen();
            }
        });

        Button credits = new TextButton("Credits", skin);
        table.add(credits);
        credits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showCreditScreen();
            }
        });
    }
    public class InputAdapter implements InputProcessor{

      	 public boolean keyDown(int keycode){
      		 if(keycode == Keys.BACK){
   					Gdx.app.exit();
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
