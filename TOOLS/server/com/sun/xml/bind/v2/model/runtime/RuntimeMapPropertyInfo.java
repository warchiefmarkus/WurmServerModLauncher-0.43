package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.MapPropertyInfo;
import java.lang.reflect.Type;

public interface RuntimeMapPropertyInfo extends RuntimePropertyInfo, MapPropertyInfo<Type, Class> {
  RuntimeNonElement getKeyType();
  
  RuntimeNonElement getValueType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeMapPropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */