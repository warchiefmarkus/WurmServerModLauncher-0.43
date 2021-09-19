package com.sun.tools.xjc.model.nav;

import com.sun.codemodel.JType;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.Outline;

public interface NType {
  JType toType(Outline paramOutline, Aspect paramAspect);
  
  boolean isBoxedType();
  
  String fullName();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\NType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */