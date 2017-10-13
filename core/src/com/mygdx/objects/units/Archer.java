package com.mygdx.objects.units;

import com.mygdx.objects.TileObject;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.objects.units.behavior.Action;
import com.mygdx.objects.units.behavior.AttackOrMove;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 18.08.2017.
 */
public class Archer extends Unit{
    public Archer(TileWithUnit tile,ObjectOwner<TileObject> owner){
        super(tile, ObjectType.Archer,owner);

        Action move = new Action(1);
        Action attack = new Action(1);
        fightingBehavior = new AttackOrMove(attack,move);

        int max = prepareTileExpenses();

        setMovingPoint(max);
        setLife(10);
        setAttackRange(2);
        setDamage(3);
        setProtection(2);
    }

    private int prepareTileExpenses(){
        tileExpenses.setExpense(TileType.Land,1);
        tileExpenses.setExpense(TileType.Hill,2);
        return tileExpenses.getMaxExpense();
    }
    @Override
    public float defend(Unit unit,float damage) {
        super.getDamage(damage);
        float retaliatory = 0;
        switch (unit.getObjectType()){
            case Infantry:
            case Archer:
                retaliatory = getDamage();
                break;
        }
        return retaliatory;
    }
}
