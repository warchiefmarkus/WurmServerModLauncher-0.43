package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.NonElementRef;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;

public interface RuntimeNonElementRef extends NonElementRef<Type, Class> {
  RuntimeNonElement getTarget();
  
  RuntimePropertyInfo getSource();
  
  Transducer getTransducer();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeNonElementRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */