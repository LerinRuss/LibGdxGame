package com.mygdx.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.MyGdxGame;
import com.mygdx.objects.units.Unit;

/**
 * Created by Viteker on 14.07.2017.
 */
public abstract class TileWithUnit extends TileWithObjects{
    public TileWithUnit(TextureRegion textureRegion, TileType type) {
        super(textureRegion, type);
    }

    public Unit getUnit() {
        return (Unit) super.getObject(level.unit);
    }
    public boolean setUnit(Unit unit) {
        if(unit.getTileExpense(type) == 0)
            return false;
        return super.setObject(unit);
    }
    public void removeUnit(){
        super.removeObject(level.unit);
    }
    public boolean isFree(){
        return getUnit() == null;
    }
}
