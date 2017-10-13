package com.mygdx.objects.units.behavior;

import com.mygdx.exceptions.runtime.BehaviorException;

/**
 * Created by Viteker on 18.08.2017.
 */
public abstract class FightingBehavior {
    final Action move;
    final Action attack;

    FightingBehavior(Action attack, Action move){
        this.attack = attack;
        this.move = move;
    }

    public abstract void attack() throws BehaviorException;
    public abstract void move() throws BehaviorException;
    public abstract void reset();
    public boolean canAttack(){
        return attack.hasAct();
    }
    public boolean canMove(){
        return move.hasAct();
    }

    public boolean canAct(){
        return canMove() | canAttack();
    }
}
