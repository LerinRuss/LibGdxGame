package com.mygdx.tiles.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithObjects;

/**
 * Created by Viteker on 10.06.2017.
 */
public class MountainTile extends TileWithObjects {
    public MountainTile(TextureRegion textureRegion) {
        super(textureRegion, TileType.Mountain);
    }
}
