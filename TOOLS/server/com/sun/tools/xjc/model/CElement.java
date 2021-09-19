package com.sun.tools.xjc.model;

import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.v2.model.core.Element;

public interface CElement extends CTypeInfo, Element<NType, NClass> {
  void setAbstract();
  
  boolean isAbstract();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */