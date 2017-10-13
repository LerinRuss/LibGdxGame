package com.mygdx.objects;

import com.mygdx.game.MyGdxGame;
import com.mygdx.tiles.TileWithObjects;

/**
 * Created by Viteker on 03.08.2017.
 */
public class Port extends TileObject {
    public Port(TileWithObjects tile, ObjectOwner owner) {
        super(tile,owner, ObjectType.Port, MyGdxGame.settings.locationSettings.building);
    }
}
