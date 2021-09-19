package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface XmlElementDecl {
  Class scope() default GLOBAL.class;
  
  String namespace() default "##default";
  
  String name();
  
  String substitutionHeadNamespace() default "##default";
  
  String substitutionHeadName() default "";
  
  String defaultValue() default "\000";
  
  public static final class GLOBAL {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\annotation\XmlElementDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */