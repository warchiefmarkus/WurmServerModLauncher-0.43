package com.sun.tools.xjc.outline;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JVar;
import com.sun.tools.xjc.model.CPropertyInfo;

public interface FieldAccessor {
  void toRawValue(JBlock paramJBlock, JVar paramJVar);
  
  void fromRawValue(JBlock paramJBlock, String paramString, JExpression paramJExpression);
  
  void unsetValues(JBlock paramJBlock);
  
  JExpression hasSetValue();
  
  FieldOutline owner();
  
  CPropertyInfo getPropertyInfo();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\FieldAccessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */