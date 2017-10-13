package com.mygdx.objects.units.behavior;

import com.mygdx.exceptions.runtime.GameLogicException;

/**
 * Created by Viteker on 18.08.2017.
 */
public class Action {
    private final int actMax;
    private int act;

    public Action(int actMax){
        if(actMax <= 0)
            throw new GameLogicException("actMax=" + actMax + " " +
                    "cannot be less or equal zero. They must have possible of attacking and moving");
        this.actMax = actMax;
    }
    public void act(){
        if(act == actMax)
            throw new GameLogicException("reached the attacks limit");
        act++;
    }
    public boolean hasAct(){
        return act != actMax;
    }
    public void reset(){
        act = 0;
    }
}
