package com.mygdx.tiles.nature;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.objects.units.Unit;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithUnit;

public class ShallowTile extends TileOwnership implements WaterTile {

    public ShallowTile(TextureRegion textureRegion) {
        super(textureRegion, TileType.Shallow);
    }

}
