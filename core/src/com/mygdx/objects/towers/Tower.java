package com.mygdx.objects.towers;

import com.mygdx.Action;
import com.mygdx.exceptions.DominationException;
import com.mygdx.exceptions.PretenderException;
import com.mygdx.game.MyGdxGame;
import com.mygdx.objects.OwnershipBuilding;
import com.mygdx.objects.ObjectOwner;
import com.mygdx.objects.ObjectType;
import com.mygdx.tiles.TileOwnership;

public class Tower extends OwnershipBuilding implements Action{
    private int power;
    private int currentDistributionRadious;

    public Tower(TileOwnership tile, ObjectOwner owner, int power){
        super(tile,owner, ObjectType.Tower, MyGdxGame.settings.locationSettings.building);
        setCurrentDistributionRadious(1);
        this.power = power;
    }



    public int getCurrentDistributionRadious() {
        return currentDistributionRadious;
    }

    void setCurrentDistributionRadious(int currentDistributionRadious) {
        this.currentDistributionRadious = currentDistributionRadious;
    }

    public void setPower(int movingPoint) {
        this.power = movingPoint;
    }
    public int getPower() {
        return power;
    }
    @Override
    public boolean act() {
        try {
            return TerritoryDistribution.squareDistribute(this);
        } catch (DominationException e) {
            e.printStackTrace();
        } catch (PretenderException e) {
            e.printStackTrace();
        }
        return false;
    }
}
