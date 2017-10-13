package com.mygdx.tiles;

import com.mygdx.game.GameLocationLevel;

/**
 * Created by Viteker on 14.08.2017.
 */
public class TilePreparer {
    public void prepare(int playersCount){
        TileOwnership.maxDominatorsCount = playersCount;
        GameLocationLevel level = new GameLocationLevel();
        TileWithObjects.level = level;
    }
}
