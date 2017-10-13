package com.mygdx.objects.units;

import com.mygdx.exceptions.runtime.GameLogicException;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.objects.TileObject;
import com.mygdx.tiles.TileWithUnit;

/**
 * Created by Viteker on 14.07.2017.
 */
public abstract class Unit extends TileObject {
    private float life;
    private float damage;
    private float protection;
    private int attackRange;

    public Unit(
            TileWithUnit tile,
            ObjectType type,
            ObjectOwner<TileObject> owner,
            int LocationLevel,
            float rotation) {

        super(tile,owner, type, LocationLevel, rotation);
    }

    void getDamage(float damage){
        float remaining = getLife() + getProtection() - damage;
        setLife(remaining);
    }


    void setAttackRange(int range) {
        if(range <= 0)
            throw new GameLogicException("AttackRange for unit cannot be less or equal zero");
        attackRange = range;
    }
    public int getAttackRange() {
        return attackRange;
    }

    void setProtection(float protection) {
        this.protection = protection;
    }
    private float getProtection() {
        return protection;
    }

    void setDamage(float damage) {
        if(damage <= 0)
            throw new GameLogicException("Exist unit can't have damage less or equal than zero");
        this.damage = damage;
    }
    float getDamage() {
        return damage;
    }

    void setLife(float life){this.life = life;}
    float getLife() {
        return life;
    }

    public TileWithUnit getTile(){
        return (TileWithUnit) super.getTile();
    }
}
