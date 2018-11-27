/*
Main class that sets the virtual width/height of our game
PPM = Pixels Per Metre... a scaling function that is needed to make object motions update realistically
*/
package com.sepr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sepr.game.Screens.PlayScreen;


public class Main extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 900;
	public static final float PPM = 100;

	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
