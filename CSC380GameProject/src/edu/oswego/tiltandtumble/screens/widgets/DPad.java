package edu.oswego.tiltandtumble.screens.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import edu.oswego.tiltandtumble.levels.BallController;

public class DPad extends Table {

	public DPad(Skin skin, BallController ballController) {
		super(skin);

		row();
		add();
		Image image = new Image(skin, "UpArrow");
		image.setName("up");
		image.addListener(ballController);
		add(image);

		row();
		image = new Image(skin, "LeftArrow");
		image.setName("left");
		image.addListener(ballController);
		add(image);

		add();

		image = new Image(skin, "RightArrow");
		image.setName("right");
		image.addListener(ballController);
		add(image);

		row();
		add();
		image = new Image(skin, "DownArrow");
		image.setName("down");
		image.addListener(ballController);
		add(image);

		pack();
	}
}
