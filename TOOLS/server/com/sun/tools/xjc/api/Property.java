package com.sun.tools.xjc.api;

import com.sun.codemodel.JType;
import javax.xml.namespace.QName;

public interface Property {
  String name();
  
  JType type();
  
  QName elementName();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\Property.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */