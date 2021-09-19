package org.fourthline.cling.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpnpAction {
  String name() default "";
  
  UpnpOutputArgument[] out() default {};
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\UpnpAction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */