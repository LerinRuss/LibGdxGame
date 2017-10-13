package com.mygdx.control.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.control.ControlHandler;
import com.mygdx.control.PlayersControl;
import com.mygdx.control.handlers.Handler;
import com.mygdx.coordinates.Coordinates;
import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.loader.AssetLoader;
import com.mygdx.player.Player;
import com.mygdx.screens.GameScreen;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.utilities.TransformCoordinates;

/**
 * Created by Viteker on 15.07.2017.
 */
public class GameInput implements InputProcessor {
    private final Handler handler;
    private OrthographicCamera camera;

    public GameInput(Handler handler, OrthographicCamera camera){
        this.handler = handler;
        this.camera = camera;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == 1 || dragged)
            return false;
        GameTiledMap.MapCoordinates coordinates = cursorTransformRotationAndTranslateCoordinatesToMap(
                MyGdxGame.gameTiledMap,
                screenX,
                screenY
        );
        return handler.handle(coordinates);
    }

    private static int x;
    private static int y;
    static boolean dragged;
    static Label label = new Label("",MyGdxGame.skin,"black");
    static{
        GameScreen.addMenuActor(label);
        label.setSize(2*MyGdxGame.settings.mapSettings.TILE_SIZE,MyGdxGame.settings.mapSettings.TILE_SIZE>>2);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }
    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        GameTiledMap.MapCoordinates coordinates = cursorTransformRotationAndTranslateCoordinatesToMap(
                MyGdxGame.gameTiledMap,
                screenX,
                screenY
        );

        int x = coordinates.getX();
        int y = coordinates.getY();

        TiledMapTile tile = MyGdxGame.gameTiledMap.getTile(x,y);
        if(!(tile instanceof TileOwnership))
            return false;
        TileOwnership tileOwnership = (TileOwnership) tile;

        com.mygdx.player.Fraction fraction = null;
        com.mygdx.player.Player player = null;
        if(tileOwnership.getTileOwner() != null) {
            fraction = ((Player)tileOwnership.getTileOwner()).getFraction();
            player = (com.mygdx.player.Player) tileOwnership.getTileOwner();
        }
        String s = "owner=" + fraction + " prtctn=" + tileOwnership.getProtectionPoint()+"\n" +
                "x=" + x + " y=" + y + "\n";

        if(player != null && player.tileOwnershipsCoordinates.containsKey(coordinates)){
            AssetLoader.MyTextureRegion myTextureRegion = (AssetLoader.MyTextureRegion) player.tileOwnershipsCoordinates.get(coordinates);
            s += "b = " + myTextureRegion.b;
        }

        label.setText(s);
        label.setPosition(screenX - label.getWidth()/2,Gdx.graphics.getHeight() - screenY);
        return true;
    }
    @Override
    public boolean scrolled(int amount) {
        camera.zoom += amount/2.;
        return true;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        dragged = true;
        Vector2 vector2 = new Vector2(screenX,screenY);
        vector2.rotate(GameScreen.rotationGame);
        screenX = (int) vector2.x;
        screenY = (int) vector2.y;

        int dx = screenX - x;
        int dy = screenY - y;
        camera.translate(-dx,dy);
        x = screenX;
        y = screenY;
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dragged = false;
        Vector2 vector2 = new Vector2(screenX,screenY);
        vector2.rotate(GameScreen.rotationGame);
        x = (int) vector2.x;
        y = (int) vector2.y;

        return true;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    GameTiledMap.MapCoordinates cursorTransformRotationAndTranslateCoordinatesToMap(GameTiledMap map, int screenX, int screenY){
        Coordinates coordinates = TransformCoordinates.cursorTransformRotation(camera,screenX,screenY);
       return TransformCoordinates.transform(map,coordinates.x, coordinates.y);
    }
}
