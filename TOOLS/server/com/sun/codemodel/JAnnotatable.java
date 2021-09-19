package com.sun.codemodel;

import java.lang.annotation.Annotation;

public interface JAnnotatable {
  JAnnotationUse annotate(JClass paramJClass);
  
  JAnnotationUse annotate(Class<? extends Annotation> paramClass);
  
  <W extends JAnnotationWriter> W annotate2(Class<W> paramClass);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnnotatable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */