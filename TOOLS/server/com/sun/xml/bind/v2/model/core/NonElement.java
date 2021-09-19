package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;

public interface NonElement<T, C> extends TypeInfo<T, C> {
  QName getTypeName();
  
  boolean isSimpleType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\NonElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */