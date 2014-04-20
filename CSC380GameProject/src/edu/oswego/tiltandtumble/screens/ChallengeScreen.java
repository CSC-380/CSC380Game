package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


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
                game.showLevelScreen();
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
