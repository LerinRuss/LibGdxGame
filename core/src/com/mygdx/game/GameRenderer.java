package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.screens.GameScreen;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

/**
 * Created by Viteker on 23.06.2017.
 */
public class GameRenderer extends OrthogonalTiledMapRenderer{
    private final int MAP_SIZE, TILE_SIZE;
    public GameRenderer(TiledMap map, float unitScale, int mapSize, int tileSize) {
        super(map, unitScale);
        this.MAP_SIZE = mapSize;
        this.TILE_SIZE = tileSize;
    }

    public void render() {
        super.render();
        //??
        renderBoardryPlayers();
    }
    @Override
    public void renderObjects(MapLayer layer) {
        for (MapObject object : layer.getObjects()) {
            renderObject(layer,object);
        }
    }
    private void renderBoardryPlayers(){
        batch.begin();
        com.mygdx.player.Player[] players = GameScreen.players;
        for (com.mygdx.player.Player player : players) {
            ObjectMap.Entries<GameTiledMap.MapCoordinates, TextureRegion> entries = player.BordaryIterator();
            while (entries.hasNext()) {
                ObjectMap.Entry<GameTiledMap.MapCoordinates, TextureRegion> entry = entries.next();
                GameTiledMap.MapCoordinates coordinates = entry.key;
                TextureRegion textureRegion = entry.value;

                int size = MAP_SIZE * TILE_SIZE;

                int y = (int) Math.floor(viewBounds.y / size);
                int y2 = (int) Math.ceil((viewBounds.y + viewBounds.height + TILE_SIZE) / size);
                int xStart = (int) Math.floor(viewBounds.x / size);
                int xEnd = (int) Math.ceil((viewBounds.x + viewBounds.width + TILE_SIZE) / size);

                for (; y < y2; y++) {
                    for (float x = xStart; x < xEnd; x++) {
                        float xx = (x * MAP_SIZE + coordinates.getX()) * TILE_SIZE,
                                yy = (y * MAP_SIZE + coordinates.getY()) * TILE_SIZE;

                        int width = textureRegion.getRegionWidth();
                        int height = textureRegion.getRegionHeight();

                        Vector2 bottomLeftPoint = new Vector2(xx,yy);
                        Vector2 topRightPoint = new Vector2(xx + width,yy + height);

                        if(viewBounds.contains(bottomLeftPoint) || viewBounds.contains(topRightPoint)){
                            batch.draw(
                                    textureRegion,
                                    xx,
                                    yy
                            );
                        }
                    }

                }
            }
        }
        batch.end();
    }
    public void renderObject(MapLayer layer,MapObject object) {
        TiledMapTileMapObject tiledMapTileMapObject = (TiledMapTileMapObject) object;

        int size = MAP_SIZE * TILE_SIZE;

        int y = (int) Math.floor( viewBounds.y/size);
        int y2 = (int) Math.ceil ((viewBounds.y + viewBounds.height+TILE_SIZE)/size);
        int xStart = (int) Math.floor(viewBounds.x/size);
        int xEnd = (int) Math.ceil((viewBounds.x + viewBounds.width + TILE_SIZE)/size);

        for(;y < y2; y++){
            for(float x = xStart; x < xEnd; x ++){
                float xx = (x * MAP_SIZE + tiledMapTileMapObject.getX()) * TILE_SIZE,
                        yy = (y * MAP_SIZE + tiledMapTileMapObject.getY()) * TILE_SIZE;

                TextureRegion region = tiledMapTileMapObject.getTextureRegion();
                int width = region.getRegionWidth();
                int height = region.getRegionHeight();

                Vector2 bottomLeftPoint = new Vector2(xx,yy);
                Vector2 topRightPoint = new Vector2(xx + width,yy + height);

                if(viewBounds.contains(bottomLeftPoint) || viewBounds.contains(topRightPoint)){

                    float rotation = tiledMapTileMapObject.getRotation();

                    batch.draw (
                            region,
                            xx,
                            yy,
                            width>>1,
                            height>>1,
                            width,
                            height,
                            unitScale,
                            unitScale,
                            rotation);
                }
            }

        }

    }
    public void renderTileLayer (TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        final int layerWidth = layer.getWidth();
        final int layerHeight = layer.getHeight();

        final float layerTileWidth = layer.getTileWidth() * unitScale;
        final float layerTileHeight = layer.getTileHeight() * unitScale;

        final int col1 = (int)(viewBounds.x / layerTileWidth);
        final int col2 = (int)((viewBounds.x + viewBounds.width + layerTileWidth) / layerTileWidth);

        final int row1 =(int)(viewBounds.y / layerTileHeight);
        final int row2 = (int)((viewBounds.y + viewBounds.height + layerTileHeight) / layerTileHeight);

        float y = row2 * layerTileHeight;
        float xStart = col1 * layerTileWidth;
        final float[] vertices = this.vertices;

        for (int row = row2; row >= row1; row--) {
            float x = xStart;
            for (int col = col1; col < col2; col++) {
                int yy = row,
                        xx = col;

                if(yy < 0){
                    yy = layerHeight + yy;
                }else if(yy >= layerHeight){
                    yy -= layerHeight;
                }
                if(xx < 0){
                    xx = layerWidth + xx;
                }else if(xx >= layerWidth){
                    xx -= layerWidth;
                }

                final TiledMapTileLayer.Cell cell = layer.getCell(xx, yy);
                if (cell == null) {
                    x += layerTileWidth;
                    continue;
                }
                final TiledMapTile tile = cell.getTile();

                if (tile != null) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() * unitScale;
                    float y1 = y + tile.getOffsetY() * unitScale;
                    float x2 = x1 + region.getRegionWidth() * unitScale;
                    float y2 = y1 + region.getRegionHeight() * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
                }
                x += layerTileWidth;
            }
            y -= layerTileHeight;
        }
    }
    public void dispose(){
    }
}
