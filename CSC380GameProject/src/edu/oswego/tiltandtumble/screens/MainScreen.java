package edu.oswego.tiltandtumble.screens;


import com.badlogic.gdx.Gdx;
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
        Gdx.input.setInputProcessor(stage);

        Window window = new Window("\nTilt and Tumble!",skin);
        window.setFillParent(true);
        stage.addActor(window);
        Table table = new Table();
        table.setFillParent(true);
        window.addActor(table);

        Button play = new TextButton("Click to Select Level!", skin);
        table.add(play);
        play.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showLevelScreen();
            }
        });

        table.row();
        Button settings = new TextButton("Click to View Settings!", skin);
        table.add(settings);
        settings.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showSettingsScreen();
            }
        });

        table.row();
        Button scores = new TextButton("Click to View Scores!", skin);
        table.add(scores);
        scores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showHighScoresScreen();
            }
        });

        table.row();
        Button credits = new TextButton("Click to View Credits!", skin);
        table.add(credits);
        credits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showCreditScreen();
            }
        });
    }
}
