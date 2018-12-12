package com.sepr.game.Sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sepr.game.Main;
import com.sepr.game.Screens.PlayScreen;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ShipTest extends GameTest {

   /* Ship ship;
    @BeforeEach
    public void setUp() {
        Main main = new Main();
        main.batch = new SpriteBatch();
        PlayScreen playScreen = new PlayScreen(main);
        ship = new Ship(playScreen);
    }*/

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void defineShip() {
//        ship.defineShip();
//        assertEquals(22000 / Main.PPM, ship.getX());
//    }

}