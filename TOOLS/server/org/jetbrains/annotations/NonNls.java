package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.TYPE, ElementType.PACKAGE})
public @interface NonNls {}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\jetbrains\annotations\NonNls.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */