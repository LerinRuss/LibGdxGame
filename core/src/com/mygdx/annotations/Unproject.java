package com.mygdx.annotations;

import java.lang.annotation.Documented;

/**
 * Created by Viteker on 06.08.2017.
 */
@Documented
public @interface Unproject {
    String author() default "Unknow";
    String version();
}
