package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ElementPropertyInfo;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public interface RuntimeElementPropertyInfo extends ElementPropertyInfo<Type, Class>, RuntimePropertyInfo {
  Collection<? extends RuntimeTypeInfo> ref();
  
  List<? extends RuntimeTypeRef> getTypes();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeElementPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */