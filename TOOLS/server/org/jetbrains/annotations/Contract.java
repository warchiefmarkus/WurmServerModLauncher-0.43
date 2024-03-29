package org.jetbrains.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Contract {
  String value() default "";
  
  boolean pure() default false;
  
  String mutates() default "";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\jetbrains\annotations\Contract.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */