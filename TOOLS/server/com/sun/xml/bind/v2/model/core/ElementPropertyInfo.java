package com.sun.xml.bind.v2.model.core;

import java.util.List;
import javax.xml.namespace.QName;

public interface ElementPropertyInfo<T, C> extends PropertyInfo<T, C> {
  List<? extends TypeRef<T, C>> getTypes();
  
  QName getXmlName();
  
  boolean isCollectionRequired();
  
  boolean isCollectionNillable();
  
  boolean isValueList();
  
  boolean isRequired();
  
  Adapter<T, C> getAdapter();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\ElementPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */