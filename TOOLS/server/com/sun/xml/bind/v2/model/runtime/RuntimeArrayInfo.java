package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ArrayInfo;
import java.lang.reflect.Type;

public interface RuntimeArrayInfo extends ArrayInfo<Type, Class>, RuntimeNonElement {
  Class getType();
  
  RuntimeNonElement getItemType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeArrayInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */