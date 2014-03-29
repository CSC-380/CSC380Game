package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import edu.oswego.tiltandtumble.Settings;
import edu.oswego.tiltandtumble.TiltAndTumble;

public class SettingsScreen extends AbstractScreen {

    public SettingsScreen(final TiltAndTumble game){
        super(game);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        final Settings settings = game.getSettings();
        
        final CheckBox useDpad = new CheckBox("Use DPad: " + (settings.isUseDpad() ? "X" : " ") , skin);
        table.add(useDpad).spaceTop(20);

        useDpad.setChecked(settings.isUseDpad());
        useDpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setUseDpad(useDpad.isChecked()); 
                useDpad.setText("Use DPad: " + (settings.isUseDpad() ? "X" : " "));
               
            }
        });

        table.row().spaceTop(10);
        

        final CheckBox debugView = new CheckBox("Debug View: " + (settings.isDebugRender() ? "X" : " ") , skin);
        table.add(debugView);

        debugView.setChecked(settings.isDebugRender());
        debugView.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setDebugRender(debugView.isChecked());
                debugView.setText("Debug View: " + (settings.isDebugRender() ? "X" : " "));
            }
        });
        table.row().spaceTop(10);

        final CheckBox music = new CheckBox("Music: " + (settings.isMusicOn() ? "X" : " "), skin);
        table.add(music);
        
        music.setChecked(settings.isMusicOn());
        music.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){
        		settings.setMusic(music.isChecked());    		
        		music.setText("Sound: " + (settings.isMusicOn() ? "X" : " "));
        	}
        });
        
        table.row().spaceTop(10);

        final CheckBox soundEffect = new CheckBox("Sound Effects: " + (settings.isSoundEffectOn() ? "X" : " ") , skin);
        table.add(soundEffect);

        soundEffect.setChecked(settings.isSoundEffectOn());
        soundEffect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundEffect(soundEffect.isChecked());
                soundEffect.setText("Sound Effects: " + (settings.isSoundEffectOn() ? "X" : " "));
            }
        });
        table.row().spaceTop(20);

        Button back = new TextButton("Go Back", skin);
        table.add(back);
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.showPreviousScreen();
            }
        });
    }
}
