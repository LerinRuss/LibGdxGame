package com.mygdx.unproject;

import com.badlogic.gdx.utils.PooledLinkedList;
import com.mygdx.exceptions.DominationException;
import com.mygdx.exceptions.PretenderException;

/**
 * Created by Viteker on 04.08.2017.
 */
@com.mygdx.annotations.Unproject(author = "Vitya Dobronov",version = "0.1")
public class Domination<T>{
    private PooledLinkedList<Pretender> pretenders;
    private Pretender dominator;

    public Domination(int maxDominatorsCount){
        pretenders = new PooledLinkedList<Pretender>(maxDominatorsCount);
    }

    public void registration(T owner,int point) throws DominationException, PretenderException {
        if(getRegisteredPretender(owner) != null)
            throw new DominationException("Registering of registered pretender");

        Pretender pretender = new Pretender(owner,point);
        pretenders.add(pretender);
        if(!isDomination()){
            dominator = pretender;
            return;
        }
        if(pretender.point > dominator.point)
            dominator = pretender;
    }
    public void addDominationPoint(T owner, int dPoint) throws DominationException {
        Pretender pretender = getRegisteredPretender(owner);
        if(pretender == null)
            throw new DominationException("The owner isn't registered");
        pretender.point += dPoint;
        if(pretender.point > dominator.point)
            dominator = pretender;
    }
    public int getDominationPoint(T owner) throws DominationException {
        Pretender pretender = getRegisteredPretender(owner);
        if(pretender == null)
            throw new DominationException("The owner isn't registered");
        return pretender.point;
    }
    public int[] getOponentsDominationPoints(T owner){
        int[] points = new int[pretenders.size()];

        pretenders.iter();
        Pretender pretender;
        for(int i = 0; (pretender = pretenders.next()) != null; i++){
            if(pretender.owner == owner)
                continue;

            points[i] = pretender.point;
        }
        return points;
    }
    public boolean removePretender(T owner){
        if(isDominator(owner)){
            removeDominator();
            return true;
        }
        pretenders.iter();
        Pretender pretender;
        while ((pretender = pretenders.next()) != null){
            if(pretender.owner == owner) {
                pretenders.remove();
                return true;
            }
        }
        return false;
    }
    public void removeDominator(){

        Pretender newDominator = null;
        int maxPoint = 0;

        pretenders.iter();
        Pretender pretender;
        while ((pretender = pretenders.next()) != null){
            if(pretender == dominator){
                pretenders.remove();
                continue;
            }
            if(pretender.point > maxPoint){
                maxPoint = pretender.point;
                newDominator = pretender;
            }
        }
        dominator = newDominator;
    }


    public boolean isDominator(T owner){
        if(!isDomination())
            return false;
        Pretender pretender = getRegisteredPretender(owner);
        return pretender == dominator;
    }
    public int getPretendersCount(){
        return pretenders.size();
    }
    public Pretender getDominator(){
        return dominator;
    }
    public boolean isDomination(){
        return dominator != null;
    }
    public boolean isRegistered(T owner) {
        return getRegisteredPretender(owner) != null;
    }

    private Pretender getRegisteredPretender(T owner){
        pretenders.iter();
        Pretender pretender = pretenders.next();
        while(pretender != null && pretender.owner != owner){
            pretender = pretenders.next();
        }
        return pretender;
    }
    public class Pretender {
        private T owner;//owner of something
        private int point;//points for what he pretends

        /**
         *
         * @param owner - who pretends
         * @param point - his points for fighting of pretension
         * @throws PretenderException happens if owner == null or points < 0
         */
        private Pretender(T owner, int point) throws PretenderException {
            if(owner == null)
                throw new PretenderException("Owner can't be null, it means owner own nothing and he can't pretend anything");
            if(point < 0)
                throw new PretenderException("ponts for pretension must be large or equal zero");
            this.owner = owner;
            this.point = point;
        }

        public T getOwner() {
            return owner;
        }

        public int getPoint() {
            return point;
        }
    }
}
