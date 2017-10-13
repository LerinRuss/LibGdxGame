package com.mygdx.player;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.Action;
import com.mygdx.coordinates.Coordinates;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.TileObject;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.objects.units.Unit;
import com.mygdx.settings.MapSettings;
import com.mygdx.tiles.TileOwnership;
import com.mygdx.utilities.TransformCoordinates;

public class Player implements com.mygdx.tiles.TileOwner,ObjectOwner<TileObject> {
    public ObjectMap<GameTiledMap.MapCoordinates,TextureRegion> tileOwnershipsCoordinates;
    private PooledLinkedList<Unit> units = new PooledLinkedList<Unit>(Integer.MAX_VALUE);
    private PooledLinkedList<Action> activeTowers = new PooledLinkedList<Action>(Integer.MAX_VALUE);
    public final PlayerTechnology technologies = new PlayerTechnology();

    private final Coordinates cameraCoordinates = new Coordinates();
    private final Fraction fraction;
    private Coordinates capital;


    //Forbid overloading
    public Player(Fraction fraction){
        this.fraction = fraction;
        int capacity = MapSettings.mapWidth * MapSettings.mapHeight;
        tileOwnershipsCoordinates = new OrderedMap<GameTiledMap.MapCoordinates, TextureRegion>(capacity);
    }

    public Fraction getFraction() {
        return fraction;
    }

    @Override
    public boolean add(TileObject object) {
        ObjectType type = object.getObjectType();
        switch (type){
            case Infantry:
                addUnit((Unit) object);
                return true;
            case Tower:
                addActiveTower((Action) object);
                return true;
        }
        return true;
    }

    @Override
    public void remove(TileObject object) {
        ObjectType type = object.getObjectType();
        switch (type){
            case Infantry:
                removeUnit((Unit) object);
                break;
            case Tower:
                removeActiveTower((Action) object);
                break;
        }
    }

    public PlayerTechnology getTechnologies() {
        return technologies;
    }

    @Override
    public ObjectMap.Entries<GameTiledMap.MapCoordinates, TextureRegion> BordaryIterator() {
        return tileOwnershipsCoordinates.iterator();
    }

    @Override
    public void addOwnerTile(TileOwnership tileOwnership, int x, int y) {
        TextureRegion textureRegion = changeBordaryTileTexture(x,y);
        GameTiledMap.MapCoordinates coordinates = MyGdxGame.gameTiledMap.getTiledCoordinates(x,y);
        if(textureRegion != null)
            tileOwnershipsCoordinates.put(coordinates,textureRegion);
        PooledLinkedList<GameTiledMap.MapCoordinates> neightborings = findNeighboringOwnTile(x,y);

        neightborings.iter();
        GameTiledMap.MapCoordinates neightboring;

        while((neightboring=neightborings.next()) != null){
            x = neightboring.getX();
            y = neightboring.getY();
            TextureRegion region = changeBordaryTileTexture(x, y);
            if(region == null) {
                tileOwnershipsCoordinates.remove(neightboring);
                continue;
            }
            tileOwnershipsCoordinates.put(neightboring,region);
        }
    }
    private PooledLinkedList<GameTiledMap.MapCoordinates> findNeighboringOwnTile(int x, int y){
        PooledLinkedList<GameTiledMap.MapCoordinates> neightborings = new PooledLinkedList<GameTiledMap.MapCoordinates>(4);
        GameTiledMap map = MyGdxGame.gameTiledMap;
        if(checkTileForOwnership(x,y + 1))
            neightborings.add(TransformCoordinates.transform(map,x,y+1));
        if(checkTileForOwnership(x,y - 1))
            neightborings.add(TransformCoordinates.transform(map,x,y - 1));
        if(checkTileForOwnership(x + 1,y))
            neightborings.add(TransformCoordinates.transform(map,x + 1,y));
        if(checkTileForOwnership(x - 1,y))
            neightborings.add(TransformCoordinates.transform(map,x - 1,y));
        return neightborings;
    }
    private TextureRegion changeBordaryTileTexture(int x, int y){
        boolean up, down, left, right;

        up = !checkTileForOwnership(x, y + 1);
        down = !checkTileForOwnership(x, y - 1);
        left = !checkTileForOwnership(x - 1, y);
        right = !checkTileForOwnership(x + 1, y);

        return MyGdxGame.assetLoader.getTextureRegionBoardaryForTower(fraction,up,down,left,right);
    }
    private boolean checkTileForOwnership(int x, int y){
        TiledMapTile tile = MyGdxGame.gameTiledMap.getSafeIndexingTile(x,y);
        if(!(tile instanceof TileOwnership))
            return false;
        TileOwnership tileOwnership = (TileOwnership) tile;
        if(tileOwnership.getTileOwner() == null || !tileOwnership.getTileOwner().equals(this))
            return false;
        return true;
    }
    @Override
    public void deleteOwnerTile(TileOwnership tileOwnership, GameTiledMap.MapCoordinates coordinates) {
        int x = coordinates.getX(),
                y = coordinates.getY();
        tileOwnershipsCoordinates.remove(coordinates);
        PooledLinkedList<GameTiledMap.MapCoordinates> neightborings = findNeighboringOwnTile(x,y);

        neightborings.iter();
        GameTiledMap.MapCoordinates neightboring;

        while((neightboring=neightborings.next()) != null){
            x = neightboring.getX();
            y = neightboring.getY();
            TileOwnership tile = (TileOwnership) MyGdxGame.gameTiledMap.getTile(x,y);
            TextureRegion region = changeBordaryTileTexture(x, y);
            tileOwnershipsCoordinates.put(neightboring,region);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (!tileOwnershipsCoordinates.equals(player.tileOwnershipsCoordinates)) return false;
        if (fraction != player.fraction) return false;
        return capital.equals(player.capital);
    }

    @Override
    public int hashCode() {
        int result = tileOwnershipsCoordinates.hashCode();
        result = 31 * result + fraction.hashCode();
        result = 31 * result + capital.hashCode();
        return result;
    }
    public void addUnit(Unit object) {
        units.add(object);
    }
    public PooledLinkedList<Unit> getUnits(){
        return units;
    }
    public void removeUnit(Unit object){
        units.iter();
        Unit unit;
        while ((unit = units.next()) != null){
            if(unit == object){
                units.remove();
            }
        }
    }
    public void addActiveTower(Action tower){
        activeTowers.add(tower);
    }
    public void removeActiveTower(Action tower){
        activeTowers.iter();
        Action action;
        while((action = activeTowers.next()) != null){
            if(action == tower) {
                activeTowers.remove();
                return;
            }
        }
    }
    public PooledLinkedList<Action> getActiveTowers(){
        return activeTowers;
    }
    public Coordinates getCapital() {
        return capital;
    }
    public void setCapital(Coordinates capital) {
        this.capital = capital;
    }
    public Coordinates getCameraCoordinates(){
        return cameraCoordinates;
    }
    public void setCameraCoordinates(int x, int y){
        cameraCoordinates.x = x;
        cameraCoordinates.y = y;
    }
}
