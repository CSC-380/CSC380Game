package edu.oswego.tiltandtumble.screens;

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

	public ChallengeScreen(final TiltAndTumble game) {
		super(game);
	}

	@Override
	public void show() {
        Gdx.input.setInputProcessor(stage);
        game.setChallengeMode(true);
		Window table = new Window("\nChallenge", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        table.row().center().uniform().padTop(50);
		table.add("Friends", "header");
		table.add("Challenge", "header");
		table.add("Challenges", "header");
		
		//TODO figure out how to obtain friends
		Button challenge = new TextButton("C", skin);
		challenge.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.showLevelScreen();
            	System.out.println(" ");
            	//Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
            	Cluster cluster = Cluster.builder().addContactPoint("129.3.20.26").withPort(2715).build();
            	Session session = cluster.connect();
            	session.execute("DROP KEYSPACE challengeMap");
            	session.execute("CREATE KEYSPACE challengeMap "
            	   		+ "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
          
            	session.execute("USE challengeMap;");
            	session.execute("CREATE TABLE users ("
            	   		+ "username text PRIMARY KEY, "
            	   		+ "highscore int, "
            	   		+ "pathx map<int, int>, "
            	   		+ "pathy map<int, int>);");
            	session.execute("INSERT INTO users (username, highscore, pathx, pathy) "
            	   		+ "VALUES ('schrecen', 0, {0 : 1}, {0 : 1});");
            	/*
            	
            	ResultSet results = session.execute("SELECT * FROM users;");
         	   	System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "username", "highscore", "path", 
         		       "-------------------------------+-----------------------+------------------------"));
         	   	List<Row> rows = results.all();
         	   	for (int i = 0; i < rows.size(); ++i) {
         	   		Map<Integer, Integer> mapx = rows.get(i).getMap("pathx", Integer.class, Integer.class);
         		    Map<Integer, Integer> mapy = rows.get(i).getMap("pathy", Integer.class, Integer.class);
         		    //List<String> list = rows.get(i).getList("path", String.class);
         		    String path = "";
         		    for(int j = 0; j < mapx.size(); ++j) {
         		    	path = path + "(" + mapx.get(j) + ", " + mapy.get(j) + ") ";
         		    }
         		    System.out.println(String.format("%-30s\t%-20s\t%-20s", 
         		    rows.get(i).getString("username"), rows.get(i).getInt("highscore"),  path));
         	   	}
         	   	*/
     
            }
           
        });
		table.row().center();
		table.add("KellyMaestri");
		table.add(challenge);
		table.add("0 challenges");

		
		table.row().expand().padBottom(10);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(4).bottom();

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	game.setChallengeMode(false);
                game.showPreviousScreen();
            }
        });
	}

}
