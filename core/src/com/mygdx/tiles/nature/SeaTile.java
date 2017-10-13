package com.mygdx.tiles.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 10.06.2017.
 */
public class SeaTile extends TileWithUnit implements WaterTile {
    public SeaTile(TextureRegion textureRegion) {
        super(textureRegion, TileType.Sea);
    }
}
