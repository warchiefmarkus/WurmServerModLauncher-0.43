package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface XmlElement {
  String name() default "##default";
  
  boolean nillable() default false;
  
  boolean required() default false;
  
  String namespace() default "##default";
  
  String defaultValue() default "\000";
  
  Class type() default DEFAULT.class;
  
  public static final class DEFAULT {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */