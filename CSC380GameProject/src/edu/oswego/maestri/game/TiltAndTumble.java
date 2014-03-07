package edu.oswego.maestri.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import screens.*;

public class TiltAndTumble extends Game {

	MainScreen mainScreen;
	SpriteBatch batch;
	

    @Override
    public void create() {
    	mainScreen = new MainScreen(this);
    	batch = new SpriteBatch();
        setScreen(mainScreen);
 
   }
    public void setMainScreen(){
    	this.setScreen(mainScreen);
    }
    

    @Override
    public void dispose() {

    }

    @Override
    public void render() {
	super.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
