package org.controlsfx.control.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionProxy {
  String id() default "";
  
  String text();
  
  String graphic() default "";
  
  String longText() default "";
  
  String accelerator() default "";
  
  String factory() default "";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\ActionProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */