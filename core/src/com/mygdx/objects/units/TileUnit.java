package com.mygdx.objects.units;

import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.exceptions.runtime.UnitPreparedException;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.objects.TileObject;
import com.mygdx.objects.units.behavior.FightingBehavior;
import com.mygdx.tiles.TileExpenses;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 28.08.2017.
 */
public abstract class TileUnit extends Unit{
    private TileExpenses tileExpenses;
    private FightingBehavior fightingBehavior;
    private int movingPoint;
    private GameTiledMap map;

    public TileUnit(
            TileWithUnit tile,
            ObjectType type,
            ObjectOwner<TileObject> owner,
            GameTiledMap map,
            int LocationLevel,
            float rotation) {
        super(tile, type, owner, LocationLevel, rotation);
        tileExpenses = new TileExpenses();
        this.map = map;
    }


    void getDamage(float damage){
        super.getDamage(damage);
        if(getLife() <= 0)
            map.remove(this);
    }

    public void attack(TileUnit target) {
        getDamage(
                target.defend(this, getDamage())
        );
        fightingBehavior.attack();
    }

    public void move(TileWithUnit newTile, GameTiledMap.MapCoordinates coordinates){
        if(newTile.setUnit(this)){
            getTile().removeUnit();
            setTile(newTile);
        }
        setCoordinates(coordinates);
        fightingBehavior.move();
    }


    boolean canAct(){
        return fightingBehavior.canAct();
    }
    boolean canAttack(){
        return fightingBehavior.canAttack();
    }
    boolean canMove(){
        return fightingBehavior.canMove();
    }
    public void refresh(){
        fightingBehavior.reset();
    }


    public int getTileExpense(TileType type){
        return getTileExpenses().getExpense(type);
    }
    public int getMinTileExpense(){
        return getTileExpenses().getMinExpense();
    }
    public int getMaxTileExpense(){
        return getTileExpenses().getMaxExpense();
    }

    void setMovingPoint(int movingPoint){
        if(movingPoint < getMaxTileExpense())
            throw new GameLogicException("Unit must pass through tile with max expense");
        this.movingPoint = movingPoint;
    }
    public int getMovingPoint() {
        if(movingPoint == 0)
            throw new UnitPreparedException("Unit moving points isn't prepared");
        return movingPoint;
    }

    public void setFightingBehavior(FightingBehavior fightingBehavior) {
        this.fightingBehavior = fightingBehavior;
    }
    public FightingBehavior getFightingBehavior() {
        return fightingBehavior;
    }

    private TileExpenses getTileExpenses(){
        if (tileExpenses.getMinExpense() == 0)
            throw new UnitPreparedException("Tile expenses for unit isn't prepared");
        return tileExpenses;
    }


    abstract float defend(TileUnit unit, float damage);
}
