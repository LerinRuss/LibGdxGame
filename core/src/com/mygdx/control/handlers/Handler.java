package com.mygdx.control.handlers;

import com.mygdx.control.input.GameInput;
import com.mygdx.game.map.GameTiledMap;

/**
 * Created by Viteker on 19.08.2017.
 */
public interface Handler {
    Handler handle(GameTiledMap.MapCoordinates coordinates);
}
