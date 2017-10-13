package com.mygdx.objects.units;

import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.game.map.GameTiledMap;
import com.mygdx.marks.MapMarks;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.unproject.UnitHandler;

/**
 * Created by Viteker on 15.07.2017.
 */
public class UnitControl {
    private boolean selected;

    private final GameTiledMap map;
    private final UnitHandler unitHandler;
    private PooledLinkedList<Unit> targets;
    private Unit unit;

    public UnitControl(GameTiledMap map) {
        this.map = map;
        this.unitHandler = new UnitHandler(map);
    }
    /**
     *
     * @param unit - which is being chosen
     * @return true if this unit can be chosen
     */
    public boolean canSelected(Unit unit, ObjectOwner owner){
        if(unit == null || !unit.canAct() || unit.getOwner() != owner)
            return selected = false;
        return selected = true;
    }
    public void select(MapMarks mapMarks, Unit unit){
        if(!selected)
            throw new GameLogicException("Unit cannot be selected");
        this.unit = unit;

        if(unit.canMove()) {
            unitHandler.findTheShortestWaysFromStartToOthers(unit);
            PooledLinkedList<UnitHandler.Node> list = unitHandler.getListTreeRoutes(unit);
            list.iter();
            UnitHandler.Node node;
             while ((node = list.next()) != null)
                 mapMarks.addMarkMove(node.x, node.y);

        }
        if(unit.canAttack()) {
            PooledLinkedList<Unit> list = unitHandler.findTargets(unit);
            targets = list;
            list.iter();
            Unit curr;
            while ((curr = list.next()) != null)
                mapMarks.addMarkAttack(curr.getX(), curr.getY());
        }
        selected = false;
    }


    /**
     *
     * @param unit
     * @return unit who attacks has ended attacking
     */
    public boolean attack(Unit unit){
        if(unit == null)
            throw new GameLogicException("Null Unit can't attack");
        if(targets == null)
            throw new GameLogicException("Needs to find targets before attacking");

        targets.iter();
        boolean contain = false;
        Unit curr;
        while ((curr = targets.next()) != null) {
            if (curr == unit){
                contain = true;
                break;
            }
        }
        if(!contain)
            return false;

        this.unit.attack(curr);

        if(this.unit.getLife() <= 0)
            map.remove(this.unit);
        if(curr.getLife() <= 0)
            map.remove(curr);

        targets = null;
        return true;
    }
    public boolean canStillAttack(){
        if(this.unit == null)
            throw new GameLogicException("Null Unit. First to choose a unit");
        return this.unit.canAttack();
    }
}
