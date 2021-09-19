package org.fourthline.cling.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpnpOutputArgument {
  String name();
  
  String stateVariable() default "";
  
  String getterName() default "";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\UpnpOutputArgument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */