package edu.oswego.tiltandtumble.screens;

import java.text.DateFormat;
import java.util.Collection;

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
import edu.oswego.tiltandtumble.data.HighScore;
//import java.util.SortedSet;


public class HighScoresScreen extends AbstractScreen {




	public HighScoresScreen(final TiltAndTumble game){
		super(game);
	}

	@Override
	public void show() {
		Gdx.input.setCatchBackKey(true);
		InputAdapter mProcessor = new InputAdapter();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);

		Window table = new Window("\nHigh Scores", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        table.row().center().uniform().padTop(50);
		table.add("Score", "header");
		table.add("Time", "header");
		table.add("Initials", "header");
		table.add("Date", "header");

		Collection<HighScore> scores = game.getHighScores().getScores();
		if (scores.isEmpty()) {
			table.row();
			table.add("No High Scores!").colspan(4).center();
		} else {
			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
			for (HighScore s : scores) {
				table.row().center();
				table.add(String.valueOf(s.getPoints()));
				table.add(s.getFormattedTime());
				table.add(s.getInitials());
				table.add(formatter.format(s.getDate()));
			}
		}

		table.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(4).bottom();

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
