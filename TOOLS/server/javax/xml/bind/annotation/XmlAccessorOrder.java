package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE})
public @interface XmlAccessorOrder {
  XmlAccessOrder value() default XmlAccessOrder.UNDEFINED;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlAccessorOrder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */