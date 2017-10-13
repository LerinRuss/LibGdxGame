package com.mygdx.control.building;

import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.control.PlayersControl;
import com.mygdx.objects.ObjectType;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileWithUnit;
import com.mygdx.tiles.nature.ShallowTile;

/**
 * Created by Viteker on 19.07.2017.
 */
public class TileBuildingAccessList {

    public PooledLinkedList<ObjectType> getBuildingAccesList(TileOwnership tile){
        PooledLinkedList<ObjectType> list = new PooledLinkedList<ObjectType>(Integer.MAX_VALUE);

        if(tile.getObject(ObjectType.Tower.ordinal()) == null)
            list.add(ObjectType.Tower);
        if(tile instanceof ShallowTile)
            list.add(ObjectType.Port);
        return list;
    }
}
