package com.sun.xml.bind.v2.model.core;

import com.sun.xml.bind.v2.model.annotation.Locatable;

public interface TypeInfo<T, C> extends Locatable {
  T getType();
  
  boolean canBeReferencedByIDREF();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\TypeInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */