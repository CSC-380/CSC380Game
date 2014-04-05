package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

//import java.util.SortedSet;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.data.*;

public class HighScoresScreen extends AbstractScreen {
	
	
    

	public HighScoresScreen(final TiltAndTumble game){
		super(game);
	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);

		Window table = new Window("\nHigh Scores", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);
        
        StringBuilder scoreString = new StringBuilder();
        HighScores score = game.getHighScores();
        /*SortedSet<HighScore> temp = score.getHighScore();
        temp.add(new HighScore("BG",1000,100));
        temp.add(new HighScore("BG",900,100));
        temp.add(new HighScore("BG",800,100));
        temp.add(new HighScore("BG",700,100));
        temp.add(new HighScore("BG",600,100));
        temp.add(new HighScore("BG",500,100));
        temp.add(new HighScore("BG",400,100));
        temp.add(new HighScore("BG",300,100));
        temp.add(new HighScore("BG",200,100));
        temp.add(new HighScore("BG",100,100));*/
        
		//I don't get this part, getHighScore should return a SortedSet, but isEmpty() is a TreeSet method, How come?
		if(score.getHighScore().isEmpty()){
			
			table.add("No HighScores Avalible!").expandY();			
			
		}
		else{
			table.row();
			table.add("       Scores    Time    Initials                       Date").expand().bottom().left();
			
			table.row();
			
			for(HighScore s : score.getHighScore()){
				
				scoreString.append(s.getPoints());
				scoreString.append("          ");
				scoreString.append(s.getTime());
				scoreString.append("          ");
				scoreString.append(s.getInitials());
				scoreString.append("          ");
				scoreString.append(s.getDate().toString());
				table.add(scoreString.toString());
				
				table.row();
				scoreString = new StringBuilder();
			}
		}
        table.row();
        Button back = new TextButton("Go Back", skin);
        table.add(back).expandY().colspan(5);

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showPreviousScreen();
            }
        });
	}
}
