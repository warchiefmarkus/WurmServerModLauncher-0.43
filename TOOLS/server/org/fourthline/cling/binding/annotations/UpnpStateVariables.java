package org.fourthline.cling.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UpnpStateVariables {
  UpnpStateVariable[] value() default {};
  
  boolean preferFields() default true;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\UpnpStateVariables.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */