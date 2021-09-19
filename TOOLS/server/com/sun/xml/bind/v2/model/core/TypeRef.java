package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;

public interface TypeRef<T, C> extends NonElementRef<T, C> {
  QName getTagName();
  
  boolean isNillable();
  
  String getDefaultValue();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\TypeRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */