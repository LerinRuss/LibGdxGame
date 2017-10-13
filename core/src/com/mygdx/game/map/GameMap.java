package com.mygdx.game.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.unproject.map.GeneratorDiamondSquareHeightMap;
import com.mygdx.unproject.map.SplittingLine;
import com.mygdx.unproject.map.TorDiamondSquare;

/**
 * Created by Viteker on 10.08.2017.
 */
public class GameMap {
    public GameTiledMap generateMap(int seed, double roughness, int mapSize,int tileSize){
        //Ganeration of Map
        double[][] heightMap = new double[mapSize][mapSize];
        GeneratorDiamondSquareHeightMap diamondSquare = new TorDiamondSquare(seed, new SplittingLine() {
            public double getSplitting(int x1, int y1, int x2, int y2) {
                return Math.sqrt((y2 - y1)*(y2 - y1) + (x2 - x1)*(x2-x1))/2;
            }
        });
        diamondSquare.generate(heightMap,roughness);

        double min = diamondSquare.getMinHeight();
        double max = diamondSquare.getMaxHeight();
        double average = diamondSquare.getAvarage();
        MapProporties mapProporties = new MapProporties(min,average,max);

        return new TilesMapRefactor().getMap(mapProporties,heightMap,mapSize,tileSize);
    }
}
