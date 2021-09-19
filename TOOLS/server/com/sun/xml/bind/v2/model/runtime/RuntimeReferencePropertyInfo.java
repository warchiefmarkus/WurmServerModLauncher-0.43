package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ReferencePropertyInfo;
import java.lang.reflect.Type;
import java.util.Set;

public interface RuntimeReferencePropertyInfo extends ReferencePropertyInfo<Type, Class>, RuntimePropertyInfo {
  Set<? extends RuntimeElement> getElements();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeReferencePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */