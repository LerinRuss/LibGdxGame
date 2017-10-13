package com.mygdx.player;

import com.mygdx.game.MyGdxGame;

/**
 * Created by Viteker on 02.08.2017.
 */
public class PlayerTechnology {
    private int townLevel = MyGdxGame.settings.objectsSettings.START_TOWER_POWER;
    public int getTownLevel(){
        return townLevel;
    }
}
