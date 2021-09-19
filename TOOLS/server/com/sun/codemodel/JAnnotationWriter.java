package com.sun.codemodel;

public interface JAnnotationWriter<A extends java.lang.annotation.Annotation> {
  JAnnotationUse getAnnotationUse();
  
  Class<A> getAnnotationType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JAnnotationWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */