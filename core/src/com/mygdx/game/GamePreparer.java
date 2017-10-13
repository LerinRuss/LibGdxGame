package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.mygdx.game.map.GameMap;
import com.mygdx.game.map.GameMapHandle;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.TileObject;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.towers.Tower;
import com.mygdx.objects.units.Infantry;
import com.mygdx.objects.units.Unit;
import com.mygdx.player.Player;
import com.mygdx.screens.GameScreen;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 12.08.2017.
 */
public class GamePreparer {
    private final MyGdxGame game;
    public GamePreparer(MyGdxGame game){
        this.game = game;
    }
    void start(Player[] players, int xx, int yy){
        GameScreen screen = new GameScreen(players,game,xx,yy);
        game.setScreen(screen);
    }
    public GameTiledMap prepareMap(Player[] players, int mapSize, int tileSize){
        GameTiledMap map = new GameMap().generateMap(1441355649, 0.5,mapSize,tileSize);

        int countLayers = MyGdxGame.settings.locationSettings.count;
        for(int i =  1; i < countLayers; i++){
            map.addMapLayer(new MapLayer());
        }
        GameMapHandle gameMapHandle = new GameMapHandle(map);
        int length = players.length;


        GameTiledMap.MapCoordinates[] coordinates = null;

        coordinates = gameMapHandle.captureStartTiles(players,players[0].getTechnologies().getTownLevel());

        Tower[] towers = new Tower[length];
        for (int i = 0; i < towers.length; i++) {
            TiledMapTile tiledMapTile = map.getTile(coordinates[i]);
            assert tiledMapTile instanceof TileOwnership;
            TileOwnership tile = (TileOwnership) tiledMapTile;
            towers[i] = new Tower(tile, players[i], players[i].getTechnologies().getTownLevel());
            towers[i].setTextureRegion(MyGdxGame.assetLoader.getFractionTextureRegion(players[i].getFraction(),towers[i].getObjectType()));
            towers[i].setX(coordinates[i].getX());
            towers[i].setY(coordinates[i].getY());
        }
        for (int i = 0; i < players.length; i++) {
            int x = (int) towers[i].getX();
            int y = (int) towers[i].getY();
            players[i].setCameraCoordinates(x,y);
        }
        Unit[] units = new Unit[length];
        for (int i = 0; i < units.length; i++) {
            TileWithUnit tile = towers[i].getTile();
            ObjectOwner owner = towers[i].getOwner();
            int movingPoint = MyGdxGame.settings.objectsSettings.INFANTRY_MOVING_POINTS;
            units[i] = new Infantry(tile,owner);
            TextureRegion region = MyGdxGame.assetLoader.getFractionTextureRegion(players[i].getFraction(), units[i].getObjectType());
            units[i].setTextureRegion(region);
            units[i].setX(towers[i].getX());
            units[i].setY(towers[i].getY());
        }

        for (int i = 0; i < length; i++) {

            map.addObject((TileObject) towers[i]);
            map.addObject(units[i]);
        }
        return map;
    }
}
