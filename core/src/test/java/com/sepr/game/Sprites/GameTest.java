package com.sepr.game.Sprites;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.sepr.game.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class GameTest {

    static Application application;

    @BeforeAll
    public static void init(){
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        application = new HeadlessApplication(new Main());
    }

    @AfterAll
    public static void cleanUp() {
       application.exit();
       application=null;
    }

}
