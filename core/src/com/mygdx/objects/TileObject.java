package com.mygdx.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.exceptions.runtime.MapCoordinatesException;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.settings.MapSettings;
import com.mygdx.tiles.TileWithObjects;

/**
 * Created by Viteker on 20.07.2017.
 */
public abstract class TileObject extends TiledMapTileMapObject{
    private final int locationLevel;
    private final ObjectType type;
    private ObjectOwner<TileObject> owner;

    public TileObject(
            TileWithObjects tile,
            ObjectOwner<TileObject> owner,
            ObjectType type,
            int locationLevel,
            float rotation){

        super(tile, true,true);

        setRotation(rotation);
        this.type = type;
        this.locationLevel = locationLevel;
        this.owner = owner;
    }
    public int getLocationLevel() {
        return locationLevel;
    }

    public ObjectType getObjectType(){
        return type;
    }

    public ObjectOwner<TileObject> getOwner(){
        return owner;
    }

    public void setOwner(ObjectOwner<TileObject> owner){
        this.owner = owner;
    }

    public TileWithObjects getTile(){
        return (TileWithObjects) super.getTile();
    }

    public void setTile(TiledMapTile tile){
        if(!(tile instanceof TileWithObjects))
            throw new GameLogicException("You can set only TileWithObject tile for TileObject");
        super.setTile(tile);
    }
    public void setCoordinates(GameTiledMap.MapCoordinates coordinates){
        setX(coordinates.getX());
        setY(coordinates.getY());
    }
    public void setX(float x){
        if(x < 0 || x >= MapSettings.mapWidth)
            throw new MapCoordinatesException("x = " + x + ", coordinate of object of TileObject is out of bounds of Map");
        super.setX(x);
    }
    public void setY(float y){
        if(y < 0 || y >= MapSettings.mapWidth)
            throw new MapCoordinatesException("y = " + y + ", coordinate of object of TileObject is out of bounds of Map");
        super.setY(y);
    }
}
