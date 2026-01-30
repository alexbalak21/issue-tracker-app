package app.security;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Ownership {
    OwnershipType value() default OwnershipType.ALL;
}
