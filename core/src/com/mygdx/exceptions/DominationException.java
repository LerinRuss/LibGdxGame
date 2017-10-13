package com.mygdx.exceptions;

/**
 * Throws exception when your T owner isn't registered or null
 * Created by Viteker on 04.08.2017.
 */
public class DominationException extends Throwable {
    public DominationException(String s) {
        super(s);
    }
}
