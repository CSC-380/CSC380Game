package edu.oswego.tiltandtumble.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.screens.dialogs.PauseDialog;
import edu.oswego.tiltandtumble.screens.dialogs.SelectDialog;


public class MainScreen extends AbstractScreen {

	private Dialog selectDialog;
    public MainScreen(final TiltAndTumble game) {
        super(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        
        Window window = new Window("\nTilt and Tumble", skin);
        window.setFillParent(true);
        window.setModal(true);
        window.setMovable(false);
        stage.addActor(window);
        Table table = new Table();
        table.setFillParent(true);
        table.bottom();
        window.addActor(table);

        Button play = new TextButton("Single Player", skin);
        table.add(play).width(150).pad(10).padBottom(20).colspan(4);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showLevelScreen();
            }
        });
        
        table.row();
        Button play2 = new TextButton("Multi-Player", skin);
        table.add(play2).width(150).pad(10).padBottom(20).colspan(4);
        play2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMulti();
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
    
    public void showMulti(){
    	selectDialog = new SelectDialog("MultiPlayer", skin, game, this).show(stage);
    }
}
