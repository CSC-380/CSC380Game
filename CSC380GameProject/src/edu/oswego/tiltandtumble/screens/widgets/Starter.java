package edu.oswego.tiltandtumble.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.oswego.tiltandtumble.screens.GameScreen;

public class Starter extends Dialog {
	private static final float MAX_COUNT = 4;

	private final GameScreen screen;
	private final Skin skin;
	private State currentState;
	private float countdownTime;
	private int lastCount;

	public Starter(GameScreen screen, Skin skin) {
		super("", skin, "countdown");
		this.screen = screen;
		this.skin = skin;
		setFillParent(true);
        setModal(true);
        setMovable(false);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		currentState.act(this, delta);
	}

	@Override
	public Dialog show(Stage stage) {
        currentState = State.WAITING;
        countdownTime = 0;
        lastCount = 0;

		final TextButton button = new TextButton("Start", skin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				button.remove();
				currentState = State.COUNTING;
			}
		});
		getContentTable().add(button);

		return super.show(stage);
	}

	private static enum State {
		COUNTING {
			@Override
			public void act(Starter s, float delta) {
				s.countdownTime += delta;
				if (s.countdownTime > MAX_COUNT) {
					s.hide();
					s.screen.start();
					return;
				}

				int count = (int)Math.floor(MAX_COUNT - s.countdownTime);
				if (s.lastCount != count) {
					s.clear();
					Label text = new Label(String.valueOf(count), s.skin, "countdown");
					s.add(text);
					s.pack();
				}
				s.lastCount = count;
			}
		},
		WAITING;

		public void act(Starter s, float delta) {}
	}
}
