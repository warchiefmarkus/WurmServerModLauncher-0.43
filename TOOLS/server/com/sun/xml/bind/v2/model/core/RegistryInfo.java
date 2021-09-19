package com.sun.xml.bind.v2.model.core;

import java.util.Set;

public interface RegistryInfo<T, C> {
  Set<TypeInfo<T, C>> getReferences();
  
  C getClazz();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\RegistryInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */