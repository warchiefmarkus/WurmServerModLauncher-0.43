package com.sun.tools.xjc.outline;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.model.CPropertyInfo;

public interface FieldOutline {
  ClassOutline parent();
  
  CPropertyInfo getPropertyInfo();
  
  JType getRawType();
  
  FieldAccessor create(JExpression paramJExpression);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\FieldOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */