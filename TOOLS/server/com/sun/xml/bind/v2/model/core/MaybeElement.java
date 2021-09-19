package com.sun.xml.bind.v2.model.core;

import javax.xml.namespace.QName;

public interface MaybeElement<T, C> extends NonElement<T, C> {
  boolean isElement();
  
  QName getElementName();
  
  Element<T, C> asElement();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\MaybeElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */