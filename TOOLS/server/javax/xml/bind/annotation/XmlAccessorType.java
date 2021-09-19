package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface XmlAccessorType {
  XmlAccessType value() default XmlAccessType.PUBLIC_MEMBER;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlAccessorType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */