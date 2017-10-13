package com.mygdx.control;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.control.building.BuildingControl;
import com.mygdx.control.handlers.UnitHandler;
import com.mygdx.control.input.AttackInput;
import com.mygdx.control.input.ChoosingInput;
import com.mygdx.control.input.MovingInput;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.marks.Accentuation;
import com.mygdx.marks.MapMarks;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.units.Unit;
import com.mygdx.screens.GameScreen;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 02.08.2017.
 */
public class ControlHandler {
    public OrthographicCamera orthographicCamera;

    private ChoosingInput choosingInput;
    private MovingInput movingInput;
    private AttackInput attackInput;

    private BuildingControl buildingControl;
    private UnitHandler unitHandler;

    private MapMarks mapMarks;
    private Accentuation accentuation;
    private GameTiledMap map;

    public ControlHandler(OrthographicCamera orthographicCamera, GameTiledMap map) {
        this.orthographicCamera = orthographicCamera;
        this.map = map;
        this.mapMarks = new MapMarks(map);
        map.addObject(accentuation);

        choosingInput = new ChoosingInput(this);
        movingInput = new MovingInput(this);
        attackInput = new AttackInput(this);

        unitHandler = new UnitHandler(map);
        buildingControl = new BuildingControl(map);
    }

    public boolean choose(ObjectOwner owner, GameTiledMap.MapCoordinates coordinates){
        GameScreen.removeBuildingActors();
        accentuation.drawAccentuation(coordinates);

        if(unitHandler.choose(coordinates)) {
            if(unitHandler.hasTargets(mapMarks))
                GameScreen.addInputControl(attackInput);
            if(unitHandler.hasMoveNodes(mapMarks))
                GameScreen.addInputControl(movingInput);
            return true;
        }
        TiledMapTile tile = map.getTile(coordinates);
        if(buildingControl.build(owner,tile, coordinates))
            return true;

        return false;
    }
    public boolean move(GameTiledMap.MapCoordinates coordinates) {
        accentuation.clear();
        mapMarks.clearMarks();
        TiledMapTile tile = MyGdxGame.gameTiledMap.getTile(coordinates);
        if (!(tile instanceof TileWithUnit)) {
            unitControl.removeUnit();
            GameScreen.setInputControl(choosingInput);
            return false;
        }

        TileWithUnit tileWithUnit = (TileWithUnit) tile;
        if(!unitHandler.move(tileWithUnit,coordinates)){
            GameScreen.setInputControl(choosingInput);
            return false;
        }

        return true;
    }
    public boolean attack(GameTiledMap.MapCoordinates coordinates){
        TiledMapTile tiledMapTile = map.getTile(coordinates);
        if(!(tiledMapTile instanceof TileWithUnit) ||
                ((TileWithUnit)tiledMapTile).getUnit() == null)
            return false;

        TileWithUnit tileWithUnit = (TileWithUnit) tiledMapTile;
        Unit unit = tileWithUnit.getUnit();
        if(!unitControl.attack(unit)){
            if(unitControl.getUnit().isMoveable()){
                GameScreen.setInputControl(movingInput);
            }else {
                GameScreen.setInputControl(choosingInput);
            }
        }
        return true;
    }
    public InputProcessor getFirstInputProccessor() {
        return choosingInput;
    }
}
