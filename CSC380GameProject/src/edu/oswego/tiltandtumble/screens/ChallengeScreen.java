package edu.oswego.tiltandtumble.screens;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class ChallengeScreen extends AbstractScreen  {

	private Window topTable;
	private Window table;
	private Session session;
	private Sound button;
	private Iterator<Row> row;

	public ChallengeScreen(final TiltAndTumble game) {
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
        Gdx.input.setInputProcessor(multiplexer);
        game.setChallengeMode(true);
        session = game.getSession();
        AssetManager assetManager = new AssetManager();
        String musicFile = "data/soundfx/button-8.ogg";
		if (!assetManager.isLoaded(musicFile)) {
			assetManager.load(musicFile, Sound.class);
			assetManager.finishLoading();
		}
		button = assetManager.get(musicFile, Sound.class);
        
        this.showLevelTable();
	}
	
	private void showTopChallenges(final int levelNum){
		
		ResultSet result = session.execute("SELECT username,highscore FROM level"+(levelNum+1));
		row  = result.iterator();
     
		table = new Window("\nLevel " + (levelNum +1), skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        table.row().center().uniform().padTop(50);
        table.add("Rank", "header");
		table.add("Time", "header");
		table.add("Name", "header");
		table.add("Challenge", "header");
		
		int count = 1;
		while(row.hasNext())
		{
			final Row r = row.next();
			Button accept = new TextButton("A", skin);
			
	  		   accept.addListener(new ChangeListener(){
						@Override
			            public void changed(ChangeEvent event, Actor actor) {
							topTable.setVisible(false);
							game.setName(r.getString("username"));
							game.showGameScreen(levelNum, GameScreen.Mode.NETWORKING);
							
						}	
	  		   });
			table.row().center();
	  		table.add("" + count);
			//Row r = row.next();
			table.add("" +r.getString("username"));
			table.add("" +r.getInt("highscore"));
			table.add(accept);
			count++;
		}
		
		table.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(4).bottom();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
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
       
      
		
		//TODO figure out how to obtain friends............
        //story of her life!!!
		//Don't feel bad for her though...............
        int count = game.getLevels().size();
        for ( int i = 0; i < count; i++) {
			//if ((i % 5) == 0) {
			//	window.row().pad(10).width(75);
			//}
        	final int val = i;
			Button l = new TextButton(Integer.toString(i + 1), skin);
			topTable.add(l);
			l.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					button.play();
					topTable.setVisible(false);
					showTopChallenges(val);
					
				}
			});
		}
        
        
//		Button lvl1 = new TextButton("1", skin);
//		topTable.add(lvl1);
//		lvl1.addListener(new ChangeListener(){
//			@Override
//            public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				topTable.setVisible(false);
//				showTopChallenges(1);
//
//			}
//		});
//		Button lvl2 = new TextButton("2", skin);
//		topTable.add(lvl2);
//		lvl2.addListener(new ChangeListener(){
//			@Override
//            public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				topTable.setVisible(false);
//				showTopChallenges(2);
//			}
//			
//		});
//		Button lvl3 = new TextButton("3", skin);
//		topTable.add(lvl3);
//		lvl3.addListener(new ChangeListener(){
//			@Override
//            public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				topTable.setVisible(false);
//				showTopChallenges(3);
//			}
//			
//		});
//		Button lvl4 = new TextButton("4", skin);
//		topTable.add(lvl4);
//		lvl4.addListener(new ChangeListener(){
//			@Override
//            public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				topTable.setVisible(false);
//				showTopChallenges(4);
//			}
//			
//		});
//		Button lvl5 = new TextButton("5", skin);
//		topTable.add(lvl5);
//		lvl5.addListener(new ChangeListener(){
//			@Override
//            public void changed(ChangeEvent event, Actor actor) {
//				button.play();
//				topTable.setVisible(false);
//				showTopChallenges(5);
//			}
//			
//		});
		
		Button back = new TextButton("Go Back", skin);
		topTable.row().spaceTop(35);
		topTable.add(back).colspan(5).bottom();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	game.setChallengeMode(false);
                game.showPreviousScreen();
            }
        });
		
		
		
	}

}
