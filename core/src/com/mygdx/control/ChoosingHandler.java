package com.mygdx.control;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.BuildingMenu;
import com.mygdx.GameMultiplexer;
import com.mygdx.control.building.BuildingControl;
import com.mygdx.control.handlers.UnitHandler;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.marks.MapMarks;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.units.UnitControl;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 19.08.2017.
 */
public class ChoosingHandler{

    public boolean handle(GameMultiplexer multiplexer,
                          UnitHandler unitHandler, BuildingMenu menu,
                          MapMarks mapMarks, ObjectOwner owner,
                          GameTiledMap.MapCoordinates coordinates,
                          UnitControl unitControl, BuildingControl buildingControl) {

        menu.removeBuildingActors();
        mapMarks.getAccentuation().draw(coordinates);
        TiledMapTile tile = coordinates.getTile();
        if(tile instanceof TileWithUnit){
            TileWithUnit tileWithUnit = (TileWithUnit) tile;
            if(unitControl.canSelected(tileWithUnit.getUnit(),owner)){
                unitHandler.handle(multiplexer,mapMarks, unitControl);
                multiplexer.replace();
                return true;
            }
        }
        if(buildingControl.build(owner,tile, coordinates))
            return true;

        return false;
    }
}
