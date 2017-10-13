package com.mygdx.game.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mygdx.game.MyGdxGame;
import com.mygdx.tiles.TileType;

/**
 * @author Viktor Dobronov
 */
class TilesMapRefactor{

    GameTiledMap getMap(MapProporties mapProporties, double[][] heightMap, int mapSize, int tileSize){
        TiledMapTileLayer tiledMapTileLayer = new TiledMapTileLayer(
                mapSize,
                mapSize,
                tileSize,
                tileSize);
        setMap(tiledMapTileLayer,mapProporties,heightMap);
        return new GameTiledMap(tiledMapTileLayer,mapSize, mapSize);
    }
    private void setMap(TiledMapTileLayer layer, MapProporties mapProporties, double[][] heightMap){
        for (int y = 0; y < heightMap.length; y++){
            for(int x = 0; x < heightMap[0].length; x++){
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                double value = heightMap[y][x];
                StaticTiledMapTile tile;

                if(value < mapProporties.getSeaLevel()){
                    tile = new com.mygdx.tiles.nature.SeaTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.Sea));
                }else if(value < mapProporties.getShallowLevel()){
                    tile = new com.mygdx.tiles.nature.ShallowTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.Shallow));
                }else if(value < mapProporties.getLandLevel()){
                    tile = new com.mygdx.tiles.nature.LandTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.Land));
                }else if(value < mapProporties.getHillLevel()){
                    tile = new com.mygdx.tiles.nature.HillTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.Hill));
                }else if(value < mapProporties.getMountainLevel()){
                    tile = new com.mygdx.tiles.nature.MountainTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.Mountain));
                }else{
                    tile = new com.mygdx.tiles.nature.EternalFrostTile(MyGdxGame.assetLoader.getNatureTextureRegion(TileType.EternalFrost));
                }

                layer.setCell(x,y,cell.setTile(tile));
            }
        }
    }
}
