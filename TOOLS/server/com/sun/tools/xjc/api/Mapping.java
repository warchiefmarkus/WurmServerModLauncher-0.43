package com.sun.tools.xjc.api;

import java.util.List;
import javax.xml.namespace.QName;

public interface Mapping {
  QName getElement();
  
  TypeAndAnnotation getType();
  
  List<? extends Property> getWrapperStyleDrilldown();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\Mapping.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */