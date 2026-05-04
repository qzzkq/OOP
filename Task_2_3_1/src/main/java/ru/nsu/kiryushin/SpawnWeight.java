package ru.nsu.kiryushin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that sets the weighted spawn probability for a {@link Food} subclass.
 * Higher values make a food type appear more often relative to others.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpawnWeight {
    /**
     * Relative spawn weight; defaults to 10.
     *
     * @return weight value
     */
    int value() default 10;
}