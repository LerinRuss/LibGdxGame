package com.mygdx.objects;

import com.badlogic.gdx.maps.MapObject;
import com.mygdx.exceptions.DominationException;
import com.mygdx.objects.towers.Tower;

/**
 * Created by Viteker on 20.07.2017.
 */
public interface GameObjectOnTileAddition {
    boolean addObject(Tower tower) throws DominationException;
    boolean addObject(TileObject object);

    void remove(TileObject object);
    void addObject(MapObject object);
    void remove(MapObject object);
}
