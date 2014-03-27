package edu.oswego.tiltandtumble.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
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
    Preferences prefs = Gdx.app.getPreferences("My Preferences");
    String dpadVal = "DPad Value";
    String debugVal = "Debug Value";
    String musicVal = "Music Value";
    String sEffVal = "Sound Effect Value";
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table table = new Table(skin);
        table.setFillParent(true);
        stage.addActor(table);

        final Settings settings = game.getSettings();
        settings.setUseDpad(prefs.getBoolean(dpadVal));
        table.add("Use DPad: ");
        final CheckBox useDpad = new CheckBox("", skin);
        table.add(useDpad).spaceTop(20);

        useDpad.setChecked(settings.isUseDpad());
        useDpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setUseDpad(useDpad.isChecked());
                prefs.putBoolean(dpadVal,settings.isUseDpad());
                prefs.flush();
            }
        });

        table.row().spaceTop(10);

        table.add("Debug View: ");
        final CheckBox debugView = new CheckBox("", skin);
        table.add(debugView);

        debugView.setChecked(settings.isDebugRender());
        debugView.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setDebugRender(debugView.isChecked());
                prefs.putBoolean(debugVal,settings.isDebugRender());
                prefs.flush();
            }
        });
        table.row().spaceTop(10);

        settings.setMusic(prefs.getBoolean(musicVal));
        table.add("Music: ");
        final CheckBox music = new CheckBox("", skin);
        table.add(music);

        music.setChecked(settings.isMusicOn());
        music.addListener(new ChangeListener(){
        	@Override
        	public void changed(ChangeEvent event, Actor actor){
        		settings.setMusic(music.isChecked());
        		prefs.putBoolean(musicVal,settings.isMusicOn());
                prefs.flush();
        	}
        });

        table.row().spaceTop(10);

        settings.setSoundEffect(prefs.getBoolean(sEffVal));
        table.add("Sound Effects: ");
        final CheckBox soundEffect = new CheckBox("", skin);
        table.add(soundEffect);

        soundEffect.setChecked(settings.isSoundEffectOn());
        soundEffect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                settings.setSoundEffect(soundEffect.isChecked());
                prefs.putBoolean(sEffVal,settings.isSoundEffectOn());
                prefs.flush();
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
