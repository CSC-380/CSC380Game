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



public class CreditScreen extends AbstractScreen {

	public CreditScreen(final TiltAndTumble game){
		super(game);
	}

	@Override
	public void show() {
		InputAdapter mProcessor = new InputAdapter();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);

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
