package com.mygdx.objects;

import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.objects.towers.Tower;
import com.mygdx.objects.units.Infantry;
import com.mygdx.player.Player;
import com.mygdx.tiles.TileOwnership;

/**
 * Created by Viteker on 11.08.2017.
 */
public class ObjectCreater {
    public static TileObject getObject(TileOwnership tile, ObjectOwner owner, ObjectType type){
        TileObject object = null;
        switch (type){
            case Infantry:
                object = new Infantry(tile,owner);
                break;
            case Port:
                object = new Port(tile,owner);
                break;
            case Tower:
                if(owner == null) {
                    object = new Tower(tile, owner, 0);
                    break;
                }
                if(!(owner instanceof Player))
                    throw new GameLogicException("to create Tower it needs that owner is a pLayer");
                Player player = (Player) owner;
                object = new Tower(tile, owner,player.getTechnologies().getTownLevel());
                break;
        }
        return object;
    }
}
