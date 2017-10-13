package com.mygdx.tiles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.exceptions.runtime.PreparationException;
import com.mygdx.objects.towers.Tower;
import com.mygdx.exceptions.DominationException;
import com.mygdx.exceptions.PretenderException;
import com.mygdx.screens.GameScreen;
import com.mygdx.unproject.Domination;

public abstract class TileOwnership extends TileWithUnit {
    static int maxDominatorsCount;
    private Domination<TileOwner> domination;
    private TileOwner tileOwner;
    private int protectionPoint;//may be less zero, it means that this tile with owner are under occupation

    public TileOwnership(TextureRegion textureRegion, TileType type) {
        super(textureRegion, type);
        if(maxDominatorsCount == 0)
            throw new PreparationException("max count of Dominators isn't prepared");
        domination = new Domination<TileOwner>(maxDominatorsCount);
    }

    public boolean installOwner(){
        if(domination.getPretendersCount() == 0)
            return false;

        Domination.Pretender pretender = domination.getDominator();
        TileOwner newOwner = (TileOwner) pretender.getOwner();
        int protectionPoint = calculateProtectionPoint(
                pretender.getPoint(),
                domination.getOponentsDominationPoints(newOwner)
        );
        if(protectionPoint <= 0)
            return false;

        return installOwner(newOwner,protectionPoint);
    }
    private boolean installOwner(TileOwner tileOwner, int protectionPoint){
        if(!isOverthrow())
            return false;
        this.tileOwner = tileOwner;
        this.protectionPoint = protectionPoint;
        return true;
    }
    /**
     *
     * @param tileOwner
     * @param dominationPoint
     * @return true, if tileOwner is dominator, false else
     * @throws DominationException
     * @throws PretenderException
     */
    public boolean pretend(TileOwner tileOwner, int dominationPoint) throws DominationException, PretenderException {
        domination.registration(tileOwner,dominationPoint);
        if(isOwner())
            standPower(tileOwner,dominationPoint);
        return domination.isDominator(tileOwner);
    }

    /**
     *
     * @param tileOwner
     * @param dPoint
     * @return true, if tileOwner is dominator, false else
     * @throws DominationException
     */
    public boolean addPretend(TileOwner tileOwner, int dPoint) throws DominationException {
        domination.addDominationPoint(tileOwner,dPoint);
        if(isOwner())
            standPower(tileOwner,dPoint);
        return domination.isDominator(tileOwner);
    }
    private void standPower(TileOwner owner, int dominationPoint) {
        if(owner == tileOwner)
            this.protectionPoint += dominationPoint;
        else
            this.protectionPoint -=dominationPoint;

        if(this.protectionPoint < 0)
            this.protectionPoint = 0;
    }
    /**
     *
     * @param owner - To whom it should take pretensions off
     * @return 'true' if have to choose new tile owner (if owner is tile owner), 'else' otherwise
     * @throws DominationException
     */
    public boolean withdrawPretension(TileOwner owner) throws DominationException {
        int points = domination.getDominationPoint(owner);//check of Null and Registration
        domination.removePretender(owner);
        if(owner == tileOwner) {
            tileOwner = null;
            protectionPoint = 0;
            return true;
        }
        protectionPoint += points;
        return false;
    }
    private int calculateProtectionPoint(int point, int[] oponentsPoints){
        int sum = 0;
        for (int i : oponentsPoints) {
            sum += i;
        }
        return point - sum;
    }
    public TileOwner getTileOwner() {
        return tileOwner;
    }
    public int getProtectionPoint() {
        return protectionPoint;
    }
    public boolean isOverthrow() {
        Tower tower = super.getTower();
        if(tower == null)
            return true;
        return tower.getOwner() == null;
    }
    public boolean isOwner(){
        return tileOwner != null;
    }
    public boolean isOwner(TileOwner owner){return tileOwner == owner;}
    public boolean isPretending(TileOwner owner){
        return domination.isRegistered(owner);
    }
}
