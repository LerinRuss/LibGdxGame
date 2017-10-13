package com.mygdx.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.map.GameTiledMap;

public interface TileOwner {
    ObjectMap.Entries<GameTiledMap.MapCoordinates, TextureRegion> BordaryIterator();

    void addOwnerTile(TileOwnership tileOwnership, int x, int y);
    void deleteOwnerTile(TileOwnership tileOwnership, GameTiledMap.MapCoordinates coordinates);
}
