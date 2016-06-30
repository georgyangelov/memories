package net.gangelov.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hook {
    enum Type {
        BEFORE_CREATE,
        BEFORE_UPDATE,
        BEFORE_SAVE,

        AFTER_CREATE,
        AFTER_UPDATE,
        AFTER_SAVE
    };

    Type on();
}
