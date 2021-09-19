package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;

public interface Element<T, C> extends TypeInfo<T, C> {
  QName getElementName();
  
  Element<T, C> getSubstitutionHead();
  
  ClassInfo<T, C> getScope();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\Element.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */