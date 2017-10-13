package com.mygdx.tiles.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithGroundBuilding;

/**
 * Created by Viteker on 10.06.2017.
 */
public class LandTile extends TileOwnership implements TileWithGroundBuilding{
    public LandTile(TextureRegion textureRegion) {
        super(textureRegion, TileType.Land);
    }

}
