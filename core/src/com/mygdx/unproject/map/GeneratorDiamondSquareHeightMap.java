package com.mygdx.unproject.map;

import java.util.Random;

public abstract class GeneratorDiamondSquareHeightMap {
    protected final Random random;
    private final int seed;
    protected double minHeight;
    protected double maxHeight;
    protected double avarage;


    protected GeneratorDiamondSquareHeightMap(int seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }

    public abstract void generate(double[][] map, double roughness);

    public int getSeed() {
        return seed;
    }
    public double getMinHeight() {
        return minHeight;
    }

    public double getMaxHeight() {
        return maxHeight;
    }

    public double getAvarage() {
        return avarage;
    }
}
