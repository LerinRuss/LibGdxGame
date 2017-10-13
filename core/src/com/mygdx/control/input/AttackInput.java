package com.mygdx.control.input;

import com.mygdx.control.ControlHandler;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;

/**
 * Created by Viteker on 16.08.2017.
 */
public class AttackInput extends GameInput  {
    public AttackInput(ControlHandler controlHandler) {
        super(controlHandler);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(dragged)
            return false;
        GameTiledMap.MapCoordinates coordinates = cursorTransformRotationAndTranslateCoordinatesToMap(
                MyGdxGame.gameTiledMap,
                screenX,
                screenY
        );
        return controlHandler.attack(coordinates);
    }
}
