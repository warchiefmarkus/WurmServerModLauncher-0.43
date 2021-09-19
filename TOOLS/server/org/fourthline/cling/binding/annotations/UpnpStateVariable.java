package org.fourthline.cling.binding.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpnpStateVariable {
  String name() default "";
  
  String datatype() default "";
  
  String defaultValue() default "";
  
  String[] allowedValues() default {};
  
  Class allowedValuesEnum() default void.class;
  
  long allowedValueMinimum() default 0L;
  
  long allowedValueMaximum() default 0L;
  
  long allowedValueStep() default 1L;
  
  Class allowedValueProvider() default void.class;
  
  Class allowedValueRangeProvider() default void.class;
  
  boolean sendEvents() default true;
  
  int eventMaximumRateMilliseconds() default 0;
  
  int eventMinimumDelta() default 0;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\binding\annotations\UpnpStateVariable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */