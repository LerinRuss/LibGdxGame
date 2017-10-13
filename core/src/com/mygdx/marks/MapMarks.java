package com.mygdx.marks;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.mygdx.exceptions.runtime.PreparationException;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.loader.AssetLoader;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Viteker on 02.08.2017.
 */
public class MapMarks {
    private AssetLoader assetLoader;
    private final Accentuation accentuation;
    private Queue<MapObject> marks = new LinkedList<MapObject>();
    private GameTiledMap map;

    public MapMarks(GameTiledMap map, AssetLoader assetLoader){
        this.map = map;
        this.assetLoader = assetLoader;
        accentuation = new Accentuation(map.getTile(0,0));
    }
    public void clearMarks(){
        for (MapObject mark : marks){
            map.remove(mark);
        }
        marks.clear();
    }
    public void addMarkMove(float x, float y){
        addMarkMove((int)x, (int) y);
    }
    public void addMarkMove(int x, int y){
        TiledMapTileMapObject tiledMapTileMapObject = new TiledMapTileMapObject(map.getSafeIndexingTile(x,y),true,true);
        tiledMapTileMapObject.setTextureRegion(assetLoader.markMove);
        tiledMapTileMapObject.setX(x);
        tiledMapTileMapObject.setY(y);
        addMark(tiledMapTileMapObject);
    }
    public void addMarkAttack(float x, float y){
        addMarkAttack((int)x, (int) y);
    }
    public void addMarkAttack(int x, int y){
        TiledMapTileMapObject tiledMapTileMapObject = new TiledMapTileMapObject(map.getSafeIndexingTile(x,y),true,true);
        tiledMapTileMapObject.setTextureRegion(assetLoader.markAttack);
        tiledMapTileMapObject.setX(x);
        tiledMapTileMapObject.setY(y);
        addMark(tiledMapTileMapObject);
    }
    private void addMark(TiledMapTileMapObject object){
        map.addObject(object);
        marks.add(object);
    }
    public Accentuation getAccentuation(){
        return accentuation;
    }
    /**
     * Created by Viteker on 15.07.2017.
     */
    public class Accentuation extends TiledMapTileMapObject {
        public Accentuation(TiledMapTile tile) {
            super(tile, true, true);
            setTextureRegion(assetLoader.clear);
        }
        public void clear(){
            setTextureRegion(assetLoader.clear);
        }
        public void draw(GameTiledMap.MapCoordinates coordinates){
            setX(coordinates.getX());
            setY(coordinates.getY());
            setTextureRegion(assetLoader.accentuation);
        }
    }
}
