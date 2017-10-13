package com.mygdx.objects.units.behavior;

/**
 * Может как и двигаться так  и атаковать. Порядок не важен
 *
 * Created by Viteker on 18.08.2017.
 */
public class MoveAndAttack extends FightingBehavior {

    MoveAndAttack(Action attack, Action move){
        super(attack,move);
    }

    public void attack(){
        attack.act();
    }
    public void move(){
        move.act();
    }
    public void reset(){
        attack.reset();
        move.reset();
    }
}
