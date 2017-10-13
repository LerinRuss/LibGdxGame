package com.mygdx.objects.units.behavior;

import com.mygdx.exceptions.runtime.BehaviorException;

/**
 * Если хоть один раз аттакова, то ходить уже нельзя, но атаковать можно пока может. Обратно и для ходьбы
 *
 * Created by Viteker on 17.08.2017.
 */
public class AttackOrMove extends FightingBehavior{
    private boolean attacking;
    private boolean moving;

    public AttackOrMove(Action attack, Action move){
        super(attack, move);
    }

    public void attack(){
        if(moving)
            throw new BehaviorException("One already moves it cannot attack");
        attacking = true;

        attack.act();
    }
    public void move(){
        if(attacking)
            throw new BehaviorException("One already attacks it cannot move");
        moving = true;
        move.act();
    }
    public void reset(){
        attacking = false;
        moving = false;
        attack.reset();
        move.reset();
    }

    @Override
    public boolean canAct() {
        if(attacking)
            return super.canAttack();
        if(moving)
            return super.canMove();
        return super.canAct();
    }

    @Override
    public boolean canAttack() {
        if(moving)
            return false;
        return super.canAttack();
    }

    @Override
    public boolean canMove() {
        if(attacking)
            return false;

        return super.canMove();
    }
}
