package org.seamless.statemachine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ElementType.TYPE})
public @interface States {
  Class<?>[] value();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\statemachine\States.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */