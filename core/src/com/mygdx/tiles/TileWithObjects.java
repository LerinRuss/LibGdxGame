package com.mygdx.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.game.GameLocationLevel;
import com.mygdx.objects.TileObject;
import com.mygdx.objects.towers.Tower;

/**
 * Created by Viteker on 20.07.2017.
 */
public abstract class TileWithObjects extends StaticTiledMapTile{
    static GameLocationLevel level;
    {
        if(level == null)
            throw new GameLogicException("TileWithObjects isn't prepared (LocationLevel)");
    }
    private final TileObject[] objects;
    protected final TileType type;

    public TileWithObjects(TextureRegion textureRegion, TileType type) {
        super(textureRegion);
        this.type = type;
        int length = level.count;
        objects = new TileObject[length];
    }

    public boolean setObject(TileObject object) {
        if(objects[object.getLocationLevel()] != null)
            return false;
        objects[object.getLocationLevel()] = object;
        return true;
    }

    public void removeObject(TileObject object){
        removeObject(object.getLocationLevel());
    }
    public void removeObject(int index){
        objects[index] = null;
    }
    public boolean containObject(TileObject object){
        return objects[object.getLocationLevel()] != null;
    }

    public TileObject getObject(int index) {
        return objects[index];
    }

    public void destroyBuildingsObjects(){
        removeObject(level.building);
    }

    public TileType getType() {
        return type;
    }

    public Tower getTower() {
        TileObject object = objects[level.building];
        if(object instanceof Tower)
            return (Tower) object;
        return null;
    }
}
