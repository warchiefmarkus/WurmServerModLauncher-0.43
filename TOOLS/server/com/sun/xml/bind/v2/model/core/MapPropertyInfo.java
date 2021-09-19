package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;

public interface MapPropertyInfo<T, C> extends PropertyInfo<T, C> {
  QName getXmlName();
  
  boolean isCollectionNillable();
  
  NonElement<T, C> getKeyType();
  
  NonElement<T, C> getValueType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\MapPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */