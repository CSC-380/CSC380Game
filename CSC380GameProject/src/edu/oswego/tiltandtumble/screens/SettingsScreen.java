package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.TiltAndTumble;
import edu.oswego.tiltandtumble.settings.Settings;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(final TiltAndTumble game){
        super(game);
    }

    @Override
    public void show() {
    	InputProcessor mProcessor = game.getProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer(stage, mProcessor);
        Gdx.input.setInputProcessor(multiplexer);
		Window table = new Window("\nSettings", skin);
		table.setFillParent(true);
		table.setModal(true);
		table.setMovable(false);
        stage.addActor(table);

        final Settings settings = game.getSettings();

        table.row().padTop(75);
        table.add("Use DPad: ").left();
        final CheckBox useDpad = new CheckBox("", skin);
        table.add(useDpad);
        useDpad.setChecked(settings.isUseDpad());
        useDpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                settings.setUseDpad(useDpad.isChecked());
            }
        });

        table.row().padTop(20);
        table.add("Debug View: ").left();
        final CheckBox debugView = new CheckBox("", skin);
        table.add(debugView);
        debugView.setChecked(settings.isDebugRender());
        debugView.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setDebugRender(debugView.isChecked());
            }
        });

        table.row().padTop(20);
        table.add("Music: ").left();
        final CheckBox music = new CheckBox("", skin);
        table.add(music);
        music.setChecked(settings.isMusicOn());
        music.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){

        		settings.setMusic(music.isChecked());
        	}
        });

        table.row().padTop(20);
        table.add("Sound Effects: ").left();
        final CheckBox soundEffect = new CheckBox("", skin);
        table.add(soundEffect);
        soundEffect.setChecked(settings.isSoundEffectOn());
        soundEffect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundEffect(soundEffect.isChecked());
            }
        });

        table.row().spaceTop(35);
        Button back = new TextButton("Go Back", skin);
        table.add(back).colspan(2);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            	settings.save();
                game.showPreviousScreen();
            }
            
        });
     
    }
}
