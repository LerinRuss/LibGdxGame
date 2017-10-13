package com.mygdx.control;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.units.Unit;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 19.08.2017.
 */
public class UnitMoveHandle {

    public boolean handle(Unit unit, GameTiledMap.MapCoordinates coordinates){
        GameTiledMap map = coordinates.getMap();

        TiledMapTile tiledMapTile = map.getTile(coordinates);
        if(!(tiledMapTile instanceof TileWithUnit))
            return false;

        TileWithUnit tile = (TileWithUnit) tiledMapTile;


    }
}
