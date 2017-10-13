package com.mygdx.objects;

import com.mygdx.player.Fraction;
import com.mygdx.player.PlayerTechnology;

/**
 * Created by Viteker on 08.08.2017.
 */
public interface ObjectOwner<T> {
    Fraction getFraction();
    boolean add(T object);
    void remove(T object);
}
