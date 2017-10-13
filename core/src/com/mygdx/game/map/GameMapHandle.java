package com.mygdx.game.map;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.exceptions.*;
import com.mygdx.exceptions.runtime.*;
import com.mygdx.tiles.TileOwner;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileWithGroundBuilding;

import java.util.Random;

/**
 * Created by Viteker on 07.07.2017.
 */
public class GameMapHandle {
    private final int MAP_WIDTH, MAP_HEIGHT;
    private final GameTiledMap map;

    public GameMapHandle(GameTiledMap map){
        this.MAP_WIDTH = map.getWidth();
        this.MAP_HEIGHT = map.getHeight();
        this.map = map;
    }

    public GameTiledMap.MapCoordinates[] captureStartTiles(TileOwner[] owners, int power) {
        GameTiledMap.MapCoordinates[] coordinates = new GameTiledMap.MapCoordinates[owners.length];
        //Locating of Towers
        for (int i = 0; i < owners.length; i++){
            Random r = new Random();
        boolean locating = true;
        int randomY,
                randomX;

        int cycle = 0;
        while (locating) {
            if(cycle > 1000)
                throw new com.mygdx.exceptions.runtime.TowerPlacingException("Obsession of tower placing");
            cycle++;

            randomY = r.nextInt(MAP_HEIGHT);
            randomX = r.nextInt(MAP_WIDTH);

            TiledMapTile cell = map.getSafeIndexingTile(randomX,randomY);

            if(!(cell instanceof TileWithGroundBuilding))
                continue;

            int count = 0;
            int length = 9;
            for(int y = randomY - 1; y <= randomY +1; y++){
                for(int x = randomX - 1; x <= randomX +1; x++){
                    TiledMapTile tiledMapTile = map.getSafeIndexingTile(x,y);
                    if(tiledMapTile instanceof TileWithGroundBuilding)
                        count++;
                }
            }
            if(count > (length>>1)) {
                TileWithGroundBuilding surface = (TileWithGroundBuilding) cell;

                if(surface.getTileOwner() != null)
                    throw new com.mygdx.exceptions.runtime.TowerPlacingException("Make normal placing of towers");

                if(!(surface instanceof TileOwnership))
                    throw new GameLogicException("Capturing start tile for first Tower may only if it is a ownership tile");

                locating = false;
                captureStartTileForTower((TileOwnership) surface, owners[i],power);
                coordinates[i] = map.getTiledCoordinates(randomX,randomY);
            }
        }
        }
        return coordinates;
    }
    private void captureStartTileForTower(TileOwnership tile, TileOwner owner, int dominationPoint){
        try {
            tile.pretend(owner,dominationPoint);
            tile.installOwner();
            boolean b = tile.isOwner(owner);
            assert b;
        } catch (DominationException e) {
            ExceptionHendler.handle(e);
        } catch (PretenderException e) {
            ExceptionHendler.handle(e);
        }
    }
}
