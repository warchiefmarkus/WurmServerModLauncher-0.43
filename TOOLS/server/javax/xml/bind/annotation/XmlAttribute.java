package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface XmlAttribute {
  String name() default "##default";
  
  boolean required() default false;
  
  String namespace() default "##default";
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */