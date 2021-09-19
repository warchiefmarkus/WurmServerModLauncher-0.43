package com.sun.tools.xjc.model;

import com.sun.tools.xjc.model.nav.NClass;
import com.sun.tools.xjc.model.nav.NType;
import com.sun.xml.bind.v2.model.core.NonElement;

public interface CNonElement extends NonElement<NType, NClass>, TypeUse, CTypeInfo {
  @Deprecated
  CNonElement getInfo();
  
  @Deprecated
  boolean isCollection();
  
  @Deprecated
  CAdapter getAdapterUse();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CNonElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */