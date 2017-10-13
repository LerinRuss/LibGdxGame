package com.mygdx.objects.towers;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.exceptions.DominationException;
import com.mygdx.exceptions.PretenderException;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.tiles.TileOwner;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.utilities.TransformCoordinates;

/**
 * Created by Viteker on 21.07.2017.
 */
public class TerritoryDistribution {
    public static boolean squareDistribute(Tower tower) throws DominationException, PretenderException {
        int power = tower.getPower();
        int radius = tower.getCurrentDistributionRadious();
        if(radius == power)
            return false;
        TileOwner townOwner = tower.getTile().getTileOwner();
        //it is if opponent captured a tile with this town
        //then it will wait till player have captured the tile back
        if(townOwner == null)
            return true;

        int towerX = (int) tower.getX();
        int towerY = (int) tower.getY();

        int constant = towerY + radius;
        int start = towerX - radius,
            end = towerX + radius;
        for(int start1 = start;start1 <= end; start1++){
            help(townOwner,power - radius, start1,constant);
        }
        constant = towerY - radius;
        for(int start1 = start;start1 <= end; start1++){
            help(townOwner,power - radius, start1,constant);
        }
        for(int start1 = towerY + radius - 1; start1 > constant; start1--){
            help(townOwner,power-radius,start,start1);
        }
        for(int start1 = towerY + radius - 1; start1 > constant; start1--){
            help(townOwner,power-radius,end,start1);
        }
        tower.setCurrentDistributionRadious(radius+1);
        return true;
    }

    private static void help(TileOwner townOwner, int power, int x, int y) throws DominationException, PretenderException {
        GameTiledMap.MapCoordinates coordinates = TransformCoordinates.transform(MyGdxGame.gameTiledMap,x,y);
        TiledMapTile tiledTile = MyGdxGame.gameTiledMap.getTile(coordinates);
        if(!(tiledTile instanceof TileOwnership))
            return;
        TileOwnership tileOwnership = (TileOwnership) tiledTile;
        if(!pretend(townOwner,tileOwnership, power))
            return;
        if(!tileOwnership.installOwner())
            return;
        townOwner.addOwnerTile(tileOwnership,coordinates.getX(), coordinates.getY());
    }

    private static boolean pretend(TileOwner townOwner, TileOwnership tileOwnership, int power) throws DominationException, PretenderException {
        if(tileOwnership.isPretending(townOwner))
                return tileOwnership.addPretend(townOwner, power);
        return tileOwnership.pretend(townOwner,power);
    }
}
