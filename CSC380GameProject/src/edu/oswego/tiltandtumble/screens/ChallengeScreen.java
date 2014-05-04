package edu.oswego.tiltandtumble.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class ChallengeScreen extends AbstractScreen  {

	private Window topTable;
	private Window table;
	private Session session;

	public ChallengeScreen(final TiltAndTumble game) {
		super(game);
	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);
        game.setChallengeMode(true);
        session = game.getSession();
        this.showLevelTable();
	}
	
	private void showTopChallenges(final int levelNum){

     
		table = new Window("\nLevel " + levelNum, skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        table.row().center().uniform().padTop(50);
        table.add("Rank", "header");
		table.add("Time", "header");
		table.add("Name", "header");
		table.add("Challenge", "header");
		
		Map<String, String> m = new HashMap<String,String>();
		m.put("DAM", "5.049");
		m.put("KMAE", "6.023");
		m.put("ZACK", "7.833");


		
		 for(int i = 1; i <= 5; i++){
	  		   Button accept = new TextButton("A", skin);
	  		   accept.addListener(new ChangeListener(){
						@Override
			            public void changed(ChangeEvent event, Actor actor) {
							topTable.setVisible(false);
							game.showGameScreen(levelNum);
						}	
	  		   });
	  		table.row().center();
	 		table.add("" + i);	 		
	 		table.add("" + m.get("KMAE"));
	 		table.add("KMAE");
	 		table.add(accept);
		 }
		
		table.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(4).bottom();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	//table.clear();
            	showLevelTable();
            }
        });
		
	}
	
	private void showLevelTable(){
		topTable = new Window("\nChallenge", skin);
        topTable.setFillParent(true);
        topTable.setModal(true);
        topTable.setMovable(false);
        stage.addActor(topTable);
        
        topTable.row().center().uniform().padTop(50).padRight(15);
       
      
		
		//TODO figure out how to obtain friends
        //story of her life!!!
		
		Button lvl1 = new TextButton("1", skin);
		topTable.add(lvl1);
		lvl1.addListener(new ChangeListener(){
			@Override
            public void changed(ChangeEvent event, Actor actor) {
				topTable.setVisible(false);
				showTopChallenges(1);

			}
		});
		Button lvl2 = new TextButton("2", skin);
		topTable.add(lvl2);
		lvl2.addListener(new ChangeListener(){
			@Override
            public void changed(ChangeEvent event, Actor actor) {
				topTable.setVisible(false);
				showTopChallenges(2);
			}
			
		});
		Button lvl3 = new TextButton("3", skin);
		topTable.add(lvl3);
		lvl3.addListener(new ChangeListener(){
			@Override
            public void changed(ChangeEvent event, Actor actor) {
				topTable.setVisible(false);
				showTopChallenges(3);
			}
			
		});
		Button lvl4 = new TextButton("4", skin);
		topTable.add(lvl4);
		lvl4.addListener(new ChangeListener(){
			@Override
            public void changed(ChangeEvent event, Actor actor) {
				topTable.setVisible(false);
				showTopChallenges(4);
			}
			
		});
		Button lvl5 = new TextButton("5", skin);
		topTable.add(lvl5);
		lvl5.addListener(new ChangeListener(){
			@Override
            public void changed(ChangeEvent event, Actor actor) {
				topTable.setVisible(false);
				showTopChallenges(5);
			}
			
		});
		
		Button back = new TextButton("Go Back", skin);
		topTable.row().spaceTop(35);
		topTable.add(back).colspan(5).bottom();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	game.setChallengeMode(false);
                game.showPreviousScreen();
            }
        });
		
		
		
	}

}
