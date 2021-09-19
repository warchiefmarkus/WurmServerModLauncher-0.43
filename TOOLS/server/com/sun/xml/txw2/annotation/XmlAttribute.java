package com.sun.xml.txw2.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface XmlAttribute {
  String value() default "";
  
  String ns() default "";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\annotation\XmlAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */