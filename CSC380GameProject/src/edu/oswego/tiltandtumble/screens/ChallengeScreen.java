package edu.oswego.tiltandtumble.screens;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import edu.oswego.tiltandtumble.TiltAndTumble;

public class ChallengeScreen extends AbstractScreen  {

	private Window topTable;
	private Window table;
	private Session session;
	private Sound button;


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
       // game.setChallengeMode(true);
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
//		String result = "";
//		
//		ArrayList<NameValuePair> nvp = new ArrayList<NameValuePair>();
//		
//		nvp.add(new BasicNameValuePair("year","1980"));
//		
//		try{
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost post = new HttpPost("http://moxie.cs.oswego.edu/~dmorgan/networking/androidTest.php")
//			post.setEntity(new UrlEncodedFormEntity(nvp));
//			HttpResponse r = client.execute(post);
//			HttpEntity entity = response.getEntity();
//			InputStream is = entity.getContent();
//			
//		}catch(){
//			
//		
//		}
//		
//		try{
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"),8);
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while((line = reader.readLine()) != null){
//				sb.append(line + "\n";)
//				
//			}
//			is.close();
//			result = 
//			
//		}catch(){
//			
//		}
		
		
		
		//System.out.println("top challenges for level" + (levelNum+1));
		ResultSet result = session.execute("SELECT username,highscore FROM level"+(levelNum+1));
		List<Row> lRow = result.all();
		ArrayList<Row> sortLRow = new ArrayList<Row>();
		
		for(Row r:lRow){
			if(sortLRow.size()>0){
				for(int i = 0; i < sortLRow.size(); i++){
					if(r.getInt("highscore")>sortLRow.get(i).getInt("highscore")){
						sortLRow.add(i,r);
						break;
					}else if(i == sortLRow.size()-1){
						sortLRow.add(r);
						break;
					}
				}
			}else{
				sortLRow.add(r);
			}
		}
		
		
		
		table = new Window("\nLevel " + (levelNum +1), skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);
        
        Table table2 = new Table(skin);
        ScrollPane scroll = new ScrollPane(table2, skin);

		table.add(scroll).expandY().padTop(40).padBottom(10);
       
        table2.row().center().uniform();
        table2.add("Rank", "header");
		table2.add("Name", "header");
		table2.add("Time", "header");
		table2.add("Challenge", "header");

		
		int count = 1;
		for(int i = 0; i < sortLRow.size();i++)
		{
			//final Row r = row.next();			
			if(sortLRow.get(i).getInt("highscore") > -1){				
				final String r = sortLRow.get(i).getString("username");
			//	System.out.println(sortLRow.get(i).getString("username"));
				Button accept = new TextButton("A", skin);				
		  		   accept.addListener(new ChangeListener(){
						@Override
			            public void changed(ChangeEvent event, Actor actor) {
							topTable.setVisible(false);
							game.setName(r);
							game.showGameScreen(levelNum, GameScreen.Mode.ACCEPT);
							
						}	
		  		   });
				table2.row().center().padTop(10);
		  		table2.add("" + count);
				table2.add("" +r);
				table2.add("" +sortLRow.get(i).getInt("highscore"));
				table2.add(accept);
				count++;
			}
		}
		
		table2.row().expand().padBottom(10).padTop(10);
        Button back = new TextButton("Go Back", skin);
        table2.add(back).colspan(4).bottom();

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
		topTable = new Window("\nChallenges", skin);
        topTable.setFillParent(true);
        topTable.setModal(true);
        topTable.setMovable(false);
        stage.addActor(topTable);
        
        topTable.row().padTop(25).colspan(5);   
        
        topTable.add("Select a Level", "highlight");
        topTable.row().pad(10, 10, 0, 10).width(75);
		//TODO figure out how to obtain friends............
        //story of her life!!!
		//Don't feel bad for her though...............
        
        int count = game.getLevels().size();
        for ( int i = 0; i < count; i++) {
        	if ((i % 5) == 0) {
				topTable.row().pad(10).width(75);
			}
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
		
		Button back = new TextButton("Go Back", skin);
		topTable.row().padBottom(10).padTop(55).bottom().colspan(5).width(100);
		topTable.add(back);

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	button.play();
            	//game.setChallengeMode(false);
                game.showPreviousScreen();
            }
        });
	}

}
