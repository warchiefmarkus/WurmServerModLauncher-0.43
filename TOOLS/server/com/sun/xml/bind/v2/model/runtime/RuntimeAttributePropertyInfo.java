package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
import java.lang.reflect.Type;

public interface RuntimeAttributePropertyInfo extends AttributePropertyInfo<Type, Class>, RuntimePropertyInfo, RuntimeNonElementRef {
  RuntimeNonElement getTarget();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeAttributePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */