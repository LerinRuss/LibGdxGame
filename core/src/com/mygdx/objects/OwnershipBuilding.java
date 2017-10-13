package com.mygdx.objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.tiles.TileOwnership;

/**
 * Created by Viteker on 08.08.2017.
 */
public abstract class OwnershipBuilding extends TileObject {
    public OwnershipBuilding(TileOwnership tile, ObjectOwner<TileObject> owner, ObjectType type, int locationLevel) {
        super(tile, owner, type, locationLevel);
    }

    @Override
    public TileOwnership getTile() {
        TiledMapTile tiledMapTile = super.getTile();
        assert tiledMapTile instanceof TileOwnership;
        return (TileOwnership) tiledMapTile;
    }
}
