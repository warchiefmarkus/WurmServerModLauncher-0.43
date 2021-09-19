package com.sun.xml.bind.v2.model.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationSource {
  <A extends Annotation> A readAnnotation(Class<A> paramClass);
  
  boolean hasAnnotation(Class<? extends Annotation> paramClass);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\AnnotationSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */