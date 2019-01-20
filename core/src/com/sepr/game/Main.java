/*
Main class that sets the virtual width/height of our game
PPM = Pixels Per Metre... a scaling function that is needed to make object motions update realistically
Test push 1
*/
package com.sepr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sepr.game.Screens.PlayScreen;

public class Main extends Game {

    //Sets the virtual game size and Box2D scale (Pixels Per Metre)

    public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 900;
	public static final float PPM = 100;


	//Used to identify collision. Powers of 2 used to simplify shifting operations

	public static final short DEFAULT_BIT = 1;
	public static final short SHIP_BIT = 2;
	public static final short DOCK_BIT = 4;

	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new PlayScreen(this));

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
