package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.TypeRef;
import java.lang.reflect.Type;

public interface RuntimeTypeRef extends TypeRef<Type, Class>, RuntimeNonElementRef {
  RuntimeNonElement getTarget();
  
  RuntimePropertyInfo getSource();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeTypeRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */