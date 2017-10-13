package com.mygdx.coordinates;

/**
 * Created by Viteker on 10.07.2017.
 */
public class Coordinates {
    public int x;
    public int y;
    public Coordinates(){
    }
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;

        Coordinates that = (Coordinates) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
