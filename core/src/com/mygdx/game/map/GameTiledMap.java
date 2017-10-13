package com.mygdx.game.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.mygdx.exceptions.DominationException;
import com.mygdx.exceptions.runtime.GameTiledMapException;
import com.mygdx.exceptions.runtime.MapCoordinatesException;
import com.mygdx.exceptions.runtime.TiledCoordinatesException;
import com.mygdx.objects.TileObject;
import com.mygdx.objects.GameObjectOnTileAddition;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.towers.Tower;
import com.mygdx.tiles.TileOwner;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.tiles.TileWithObjects;

/**
 *
 * @author Viteker
 */
public class GameTiledMap extends TiledMap implements GameObjectOnTileAddition{
    private final int width, height;

    public GameTiledMap(TiledMapTileLayer layer,int width, int height){
        super();
        super.getLayers().add(layer);
        this.width = width;
        this.height = height;

    }
    public boolean addObject(Tower tower) throws DominationException {
        if(!addObject((TileObject) tower))
            return false;
        if(tower.getOwner() == null)
            return true;
        int power = tower.getPower();
        TileOwnership tile = tower.getTile();
        ObjectOwner owner = tower.getOwner();
        assert owner instanceof TileOwner;
        tile.addPretend((TileOwner) owner,power);
        return true;
    }
    public boolean addObject(TileObject object){

        ObjectOwner<TileObject> owner = object.getOwner();
        if(owner == null)
            return add(object);
        if(!owner.add(object))
            return false;

        return add(object);
    }

    private boolean add(TileObject object) {
        TileWithObjects tile = object.getTile();
        if(!tile.setObject(object))
            return false;

        int index = object.getLocationLevel();
        getMapLayer(index).getObjects().add(object);
        return true;
    }

    public void remove(TileObject object){
        TileWithObjects tile = object.getTile();
        ObjectOwner<TileObject> owner = object.getOwner();
        if(owner != null)
            owner.remove(object);
        tile.removeObject(object);
        int index = object.getLocationLevel();
        getMapLayer(index).getObjects().remove(object);
    }
    public void addObject(MapObject object){
        int index = getLayersCount() - 1;
        getMapLayer(index).getObjects().add(object);
    }
    public void remove(MapObject object){
        int index = getLayersCount() - 1;
        getMapLayer(index).getObjects().remove(object);
    }
    public void addMapLayer(MapLayer layer) throws GameTiledMapException {
        if(layer instanceof TiledMapTileLayer)
            throw new GameTiledMapException("You can't add TiledMapTileLayer");
        super.getLayers().add(layer);
    }
    public void addMapLayers(MapLayer[] layers) throws GameTiledMapException {
        for (MapLayer layer : layers) {
            addMapLayer(layer);
        }
    }
    public void removeMapLayer(MapLayer layer) throws GameTiledMapException {
        if(layer instanceof TiledMapTileLayer)
            throw new GameTiledMapException("You can't remove TiledMapTileLayer");
        super.getLayers().remove(layer);
    }
    public void clearMapLayers(){
        MapLayers layers = super.getLayers();
        for (int i = 1; i < layers.getCount(); i++){
            layers.remove(i);
        }
    }
    public boolean reduceMapLayers(){
        MapLayers layers = super.getLayers();
        int current = layers.getCount() - 1;
        if(current == 0)
            return false;
        layers.remove(current);
        return true;
    }
    public void removeMapLayer(int index) throws GameTiledMapException {
        if(index == 0)
            throw new GameTiledMapException("You can't remove TiledMapTileLayer");
        super.getLayers().remove(index);
    }
    public MapLayer getMapLayer(int index){
        return super.getLayers().get(index);
    }
    public int getMapLayerIndex(MapLayer mapLayer){
        return super.getLayers().getIndex(mapLayer);
    }
    public MapLayer[] getMapLayers(){
        MapLayer[] layers = new MapLayer[getLayersCount() - 1];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = getMapLayer(i+1);
        }
        return layers;
    }
    /**
     * @return count of all layers including the TiledMapTileLayer
     */
    public int getLayersCount(){
        return super.getLayers().getCount();
    }

    public TiledMapTile getTile(int x, int y){
        TiledMapTileLayer layer = (TiledMapTileLayer) getMapLayer(0);
        return layer.getCell(x,y).getTile();
    }
    public TiledMapTile getTile(MapCoordinates coordinates){
        return getTile(coordinates.getX(),coordinates.getY());
    }
    public TiledMapTile getSafeIndexingTile(int x, int y){
        int yy = y,
                xx = x;

        if(yy < 0){
            yy = height + (yy % height);
        }else if(yy >= height){
            yy %= height;
        }
        if(xx < 0){
            xx = width + (xx%width);
        }else if(xx >= width){
            xx %= width;
        }

        return getTile(xx,yy);
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public MapCoordinates getTiledCoordinates(float x, float y){
        return getTiledCoordinates((int)x,(int)y);
    }
    public MapCoordinates getTiledCoordinates(int x, int y){
        MapCoordinates coordinates = null;
        try {
            coordinates = new MapCoordinates(this,x,y);
        } catch (TiledCoordinatesException e) {
            e.printStackTrace();
        }
        return coordinates;
    }
    /**
     * Класс хранит координаты, находящиеся сторого в пределах своей карты {@link GameTiledMap},
     * используя этот класс вы уверены, что координаты не выходят за рамки текущей карты.
     * Чтобы получить текущую карту, в рамках которой проверяются координаты используйте метод {@code getMap()}
     *
     * Создать объект этого класса нельзя, но можно получить его используя методы объекта {@link GameTiledMap},
     * такие как {@code getTiledCoordinates(...)} с различными параметрами. Используя эти методы, вы создаете
     * объект {@code MapCoordinates} привязанный к этой карте. В дальнейшем текущую карту можно изменить методом
     * {@code setMap(...)}
     *
     * @author Viteker
     */
    public class MapCoordinates {
        private int x, y;
        private GameTiledMap map;
        private MapCoordinates(GameTiledMap map, int x, int y) throws TiledCoordinatesException {
            if(x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight())
                throw new TiledCoordinatesException(
                        "Coordinates x = " + x + " y = " + y + " are out of map size = [width:" + map.getWidth()+
                                ";height:" + map.getHeight() + "]"
                );
            this.x = x;
            this.y = y;
            this.map = map;
        }

        public int getX() {
            return x;
        }
        /**
         * @param x - set coordinate
         * @throws TiledCoordinatesException are thrown if coordinates is out of map bound
         */
        public void setX(int x) throws TiledCoordinatesException {
            if(x < 0 || x >= map.getWidth())
                throw new TiledCoordinatesException("Coordinate x = " + x + " is out of map = [0:"+map.getWidth()+")");
            this.x = x;
        }

        public int getY() {
            return y;
        }

        /**
         * @param y - set coordinate
         * @throws TiledCoordinatesException are thrown if coordinates is out of map bound
         */
        public void setY(int y) throws TiledCoordinatesException {
            if(y < 0 || y >= map.getHeight())
                throw new TiledCoordinatesException("Coordinate y = " + y + " is out of map = [0:"+map.getHeight()+")");
            this.y = y;
        }

        /**
         *
         * @return {@link GameTiledMap} of coordinates, i.e. check of coordinates occurs within this map
         */
        public GameTiledMap getMap() {
            return map;
        }

        /**
         * sets new map for check and sets to zero for coordinates.
         * @param map - {@link GameTiledMap}
         */
        public void setMap(GameTiledMap map) {
            if(map.getWidth() <= 0 || map.getHeight() <= 0)
                throw new MapCoordinatesException("any side of map can't be equal or less than zero");
            this.x = 0;
            this.y = 0;
            this.map = map;
        }
        public TiledMapTile getTile(){
            return map.getTile(this);
        }
    }

}
