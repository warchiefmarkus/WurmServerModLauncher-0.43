package org.intellij.lang.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE})
public @interface Pattern {
  @Language("RegExp")
  String value();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\intellij\lang\annotations\Pattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */