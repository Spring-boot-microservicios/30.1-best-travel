package com.angelfg.best_travel.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME) // En tiempo de ejecucion
@Target(value = ElementType.METHOD) // Analizando un metodo
public @interface Notify {
    String value() default "none";
}
