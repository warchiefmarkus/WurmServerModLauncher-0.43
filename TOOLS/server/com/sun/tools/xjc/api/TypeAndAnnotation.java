package com.sun.tools.xjc.api;

import com.sun.codemodel.JAnnotatable;
import com.sun.codemodel.JType;

public interface TypeAndAnnotation {
  JType getTypeClass();
  
  void annotate(JAnnotatable paramJAnnotatable);
  
  boolean equals(Object paramObject);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\TypeAndAnnotation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */