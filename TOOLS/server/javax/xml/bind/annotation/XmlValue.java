package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface XmlValue {}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */