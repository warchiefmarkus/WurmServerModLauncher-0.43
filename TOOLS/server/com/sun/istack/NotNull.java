package com.sun.istack;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
public @interface NotNull {}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\NotNull.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */