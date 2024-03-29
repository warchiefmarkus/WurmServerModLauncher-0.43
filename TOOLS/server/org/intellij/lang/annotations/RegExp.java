package org.intellij.lang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
@Language("RegExp")
public @interface RegExp {
  String prefix() default "";
  
  String suffix() default "";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\intellij\lang\annotations\RegExp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */