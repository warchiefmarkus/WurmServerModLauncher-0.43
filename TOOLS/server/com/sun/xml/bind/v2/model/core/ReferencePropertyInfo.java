package com.sun.xml.bind.v2.model.core;

import java.util.Collection;
import java.util.Set;
import javax.xml.namespace.QName;

public interface ReferencePropertyInfo<T, C> extends PropertyInfo<T, C> {
  Set<? extends Element<T, C>> getElements();
  
  Collection<? extends TypeInfo<T, C>> ref();
  
  QName getXmlName();
  
  boolean isCollectionNillable();
  
  boolean isCollectionRequired();
  
  boolean isMixed();
  
  WildcardMode getWildcard();
  
  C getDOMHandler();
  
  Adapter<T, C> getAdapter();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\ReferencePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */