package com.sepr.game.Tools;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class WorldContactListener implements ContactListener {

    public WorldContactListener(){

    }


    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("Begin Contact: ", "");
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("End Contact: ", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
