/*
Heads Up Display... Fixes information at the bottom of the screen, e.g. score
 */

package com.sepr.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;

public class HUD implements Disposable {

    public Stage stage;
    private Viewport viewport; //New viewport to ensure that hud remains fixed
    private int score, influenceCount;
    private float xLocation, yLocation; //coordinates of the ship


    private Label countdownLabel, scoreLabel, influenceLabel, worldLabel, xLabel, yLabel;


    public HUD(SpriteBatch sb){
        influenceCount = 0;
        score = 0;

        viewport = new FitViewport(Main.V_WIDTH, Main.V_WIDTH, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("Score: " + "%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        influenceLabel = new Label(String.format("Influence: " + "%03d", influenceCount), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD VIEW", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        xLabel = new Label(String.format("X: " + "%01f", xLocation), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        yLabel = new Label(String.format("Y: " + "%01f", yLocation), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //Add our labels to the table, spacing them out equally with expandX and padding them by 10px
        table.add(xLabel).expandX().padBottom(2);
        table.add(yLabel).expandX().padBottom(2);
        table.add(scoreLabel).expandX().padBottom(2);
        table.add(influenceLabel).expandX().padBottom(2);
        table.add(worldLabel).expandX().padBottom(2);


        //Add table to the stage
        stage.addActor(table);

    }

    //updates the x and y coordinates and edits the labels
    public void update(float dt){
        xLocation = Math.round(PlayScreen.gamecam.position.x);
        yLocation = Math.round(PlayScreen.gamecam.position.y);
        xLabel.setText(String.format("X: " + "%01f", xLocation));
        yLabel.setText(String.format("Y: " + "%01f", yLocation));
    }


    @Override
    public void dispose() {
        stage.dispose();
    }
}
