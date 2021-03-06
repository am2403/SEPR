/*
Heads Up Display... Fixes information at the bottom of the screen, e.g. score
 */

package com.sepr.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sepr.game.Screens.CombatScreen;
import com.sepr.game.Screens.PlayScreen;
import com.sepr.game.Sprites.Fleet;
import com.sepr.game.Sprites.Ship;

public class HUD implements Disposable {

    public Stage stage;
    private Viewport viewport; //New viewport to ensure that hud remains fixed
    public int score, fleetHealth, shipHealth;

    public static final int V_WIDTH = 700;
    public static final int V_HEIGHT = 900;
    private Image img;
    private Table table;


    private Label scoreLabel, fleetHealthLabel, shipHealthLabel;

    public HUD(SpriteBatch sb){
        fleetHealth = Fleet.getFleetHealth(); //needs a getFleetHealth for the code marks.. not this kind of access
        shipHealth = Ship.getHealth();
        score = 0;

        img = new Image(new Texture(Gdx.files.local("MainMap.png")));
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        table = new Table();
        table.bottom();
        table.setFillParent(true);
        scoreLabel = new Label(String.format("Score: " + "%03d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        fleetHealthLabel = new Label(String.format("Fleet health: " + "%03d", fleetHealth), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        shipHealthLabel = new Label(String.format("Player health: " + "%03d", shipHealth), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        //Add our labels to the table, spacing them out equally with expandX and padding them by 10px

        table.add(scoreLabel).expandX().padBottom(2);
        table.add(fleetHealthLabel).expandX().padBottom(2);
        table.add(shipHealthLabel).expandX().padBottom(2);
        table.add(img).height(250).width(125).center();

        //Add table to the stage
        stage.addActor(table);

    }

    //updates the x and y coordinates and edits the labels
    public void update(float dt, PlayScreen playScreen){

        fleetHealth = playScreen.fleet.getFleetHealth();
        shipHealth = playScreen.ship.getHealth();
        updateFleetHealth(fleetHealth);
        updateShipHealth(shipHealth);
        updateScore(score);
        table.addActor(img);
        if(playScreen.fleet.getFleetHealth() < 10){
            score = 100;
        }
    }

    public void update(float dt, CombatScreen combatScreen){
        fleetHealth = combatScreen.fleet_combat.getFleetHealth();
        shipHealth = combatScreen.ship_combat.getHealth();
        updateFleetHealth(fleetHealth);
        updateShipHealth(shipHealth);
        table.removeActor(img);

    }

    private void updateFleetHealth(int fleetHealth) {
        fleetHealthLabel.setText(String.format("Fleet health: " + "%03d", fleetHealth));
    }
    private void updateShipHealth(int shipHealth) {
        shipHealthLabel.setText(String.format("Player health: " + "%03d", shipHealth));
    }
    public void updateScore(int score) {
        scoreLabel.setText(String.format("Score: " + "%03d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
