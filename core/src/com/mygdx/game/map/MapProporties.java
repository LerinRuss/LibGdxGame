package com.mygdx.game.map;

/**
 * Created by Viteker on 09.06.2017.
 */
public class MapProporties {
    private double seaLevel;
    private double shallowLevel;
    private double landLevel;
    private double hillLevel;
    private double mountainLevel;

    /*
    sea - 40%
    shallow - 10%
    land - 25%
    hill - 12.5%
    mountain - 9,375%
    eternal frost - 3,125%
    -------avarage-----
     */
    public MapProporties(double min, double average, double max){
        this.shallowLevel = average;
        double beloveSeaLevel = -(min - average);
        this.seaLevel = min + beloveSeaLevel * 8/10;
        double aboveSeaLevel = max - average;
        double halfAboveSeaLevel = aboveSeaLevel/2;
        this.landLevel = this.shallowLevel + halfAboveSeaLevel;
        halfAboveSeaLevel /= 2;
        this.hillLevel = this.landLevel + halfAboveSeaLevel;
        this.mountainLevel = this.hillLevel + halfAboveSeaLevel*3/4;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public double getShallowLevel() {
        return shallowLevel;
    }

    public double getLandLevel() {
        return landLevel;
    }

    public double getHillLevel() {
        return hillLevel;
    }

    public double getMountainLevel() {
        return mountainLevel;
    }
}
