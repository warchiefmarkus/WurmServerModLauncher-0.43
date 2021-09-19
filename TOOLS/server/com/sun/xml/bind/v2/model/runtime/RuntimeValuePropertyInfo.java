package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ValuePropertyInfo;
import java.lang.reflect.Type;

public interface RuntimeValuePropertyInfo extends ValuePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
  RuntimeNonElement getTarget();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeValuePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */