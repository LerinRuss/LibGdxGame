package com.mygdx.objects.units;

import com.mygdx.objects.TileObject;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.objects.units.behavior.Action;
import com.mygdx.objects.units.behavior.AttackOrMove;
import com.mygdx.tiles.TileType;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 11.08.2017.
 */
public class Infantry extends Unit {
    public Infantry(TileWithUnit tile, ObjectOwner<TileObject> owner) {
        super(tile, ObjectType.Infantry, owner);

        Action move = new Action(1);
        Action attack = new Action(1);
        fightingBehavior = new AttackOrMove(attack,move);

        int max = prepareTileExpenses();

        setMovingPoint(max);
        setLife(10);
        setAttackRange(1);
        setDamage(5);
        setProtection(3);
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
                retaliatory = getDamage();
                break;
        }
        return retaliatory;
    }
}
