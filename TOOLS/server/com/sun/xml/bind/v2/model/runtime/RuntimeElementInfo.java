package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ElementInfo;
import java.lang.reflect.Type;
import javax.xml.bind.JAXBElement;

public interface RuntimeElementInfo extends ElementInfo<Type, Class>, RuntimeElement {
  RuntimeClassInfo getScope();
  
  RuntimeElementPropertyInfo getProperty();
  
  Class<? extends JAXBElement> getType();
  
  RuntimeNonElement getContentType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeElementInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */