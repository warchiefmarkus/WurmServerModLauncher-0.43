package com.sun.xml.bind.v2.model.core;

public interface EnumLeafInfo<T, C> extends LeafInfo<T, C> {
  C getClazz();
  
  NonElement<T, C> getBaseType();
  
  Iterable<? extends EnumConstant> getConstants();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\EnumLeafInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */