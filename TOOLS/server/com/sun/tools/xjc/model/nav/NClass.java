package com.sun.tools.xjc.model.nav;

import com.sun.codemodel.JClass;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.Outline;

public interface NClass extends NType {
  JClass toType(Outline paramOutline, Aspect paramAspect);
  
  boolean isAbstract();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\nav\NClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */