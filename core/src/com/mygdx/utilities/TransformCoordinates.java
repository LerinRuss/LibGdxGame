package com.mygdx.utilities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.coordinates.Coordinates;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.screens.GameScreen;
import com.mygdx.objects.units.Unit;

/**
 * Created by Viteker on 19.07.2017.
 */
public class TransformCoordinates {
    public static Coordinates cursorTransformRotation(OrthographicCamera orthographicCamera, int screenX, int screenY) {
        float x = orthographicCamera.position.x;
        float y = orthographicCamera.position.y;

        float width = orthographicCamera.viewportWidth/2;
        float height = orthographicCamera.viewportHeight/2;

        screenX -= width;
        screenY = (int) (height - screenY);

        Vector2 vector = new Vector2(screenX,screenY);
        vector.rotate(-GameScreen.rotationGame);
        screenX = (int) vector.x;
        screenY = (int) vector.y;

        x += screenX*orthographicCamera.zoom;
        y += screenY*orthographicCamera.zoom;

        Coordinates coordinates = new Coordinates();
        int tileSize = MyGdxGame.settings.mapSettings.TILE_SIZE;

        coordinates.x = (int) Math.floor(x/tileSize);
        coordinates.y = (int) Math.floor(y/tileSize);

        return coordinates;
    }

    /**
     *  finds vector to unit from coordinates
     * @param unit
     * @param coordinates
     * @return shortest length of x to Unit and shortest length of y to unit with sign that defines direction
     */
    public static Vector2 findVectorToUnit(GameTiledMap map, Unit unit, GameTiledMap.MapCoordinates coordinates){
        int x = coordinates.getX(),
                y = coordinates.getY();

        float distanceX = x - unit.getX();
        float distanceY = y - unit.getY();


        float abscissaLength = distanceX <= 0 ? map.getWidth() - Math.abs(distanceX)
                : -map.getWidth() + Math.abs(distanceX);

        float ordinateLength = distanceY <= 0 ? map.getHeight() - Math.abs(distanceY)
                : -map.getHeight() + Math.abs(distanceY);

        if(Math.abs(abscissaLength) < Math.abs(distanceX))
            distanceX = abscissaLength;

        if(Math.abs(ordinateLength) < Math.abs(distanceY))
            distanceY = ordinateLength;

        x = (int) distanceX;
        y = (int) distanceY;

        Vector2 vector = new Vector2(x,y);

        return vector;
    }
    public static GameTiledMap.MapCoordinates transform(GameTiledMap map, int x, int y){
        int width = map.getWidth();
        int height = map.getHeight();

        x %= width;
        y %= height;

        if(x < 0)
            x = width + x;
        if(y < 0)
            y = height + y;
        return map.getTiledCoordinates(x,y);
    }
}
