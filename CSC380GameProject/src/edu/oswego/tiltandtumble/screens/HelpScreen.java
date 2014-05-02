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

public class HelpScreen extends AbstractScreen {

    public HelpScreen(final TiltAndTumble game) {
        super(game);
    }

    @Override
    public void show() {
    	InputAdapter mProcessor = new InputAdapter();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);

		Window table = new Window("\nHelp", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);
        table.row();
        //Help
        table.add("Play:", "header").expand().bottom();
        table.row();
        table.add("The game will begin will a 3-second count down.");       
        table.row();  
        table.add("Tilt the ball to move. There are many obstacles,");
        table.row();
        table.add("your main goal is to get to the red finish line.");
        table.row();
        table.add("Setting: ","header").expandX();
        table.row();
        table.add("Dpad, debug mode, music and sound effect can be");
        table.row();
        table.add("enabled or disabled inside settings.");
        table.row();
        table.add("HighScore: ", "header").expandX();
        table.row();
        table.add("You can check the top ten high scores you achieved"); 
        table.row();
        table.add("in the game.");
        
        table.row().expand().padBottom(10);
        
        Button back = new TextButton("Go Back", skin);
        table.add(back).bottom();
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
