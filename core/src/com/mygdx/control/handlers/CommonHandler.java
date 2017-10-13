package com.mygdx.control.handlers;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.control.PlayersControl;
import com.mygdx.control.input.GameInput;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.marks.MapMarks;
import com.mygdx.objects.units.Unit;
import com.mygdx.objects.units.UnitControl;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 20.08.2017.
 */
public class CommonHandler implements Handler {
    GameTiledMap map;
    UnitControl unitControl;
    PlayersControl playersControl;
    MapMarks mapMarks;

    @Override
    public Handler handle(GameTiledMap.MapCoordinates coordinates) {
        TiledMapTile tile = map.getTile(coordinates);
        if(tile instanceof TileWithUnit){
            Unit unit = ((TileWithUnit) tile).getUnit();
            if(unitControl.canSelected(unit,playersControl.current)){
                unitControl.select(mapMarks,unit);
                return new UnitHandler();
            }
        }
    }
}
