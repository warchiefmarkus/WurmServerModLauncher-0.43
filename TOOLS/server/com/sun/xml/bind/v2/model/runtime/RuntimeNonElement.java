package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.NonElement;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;

public interface RuntimeNonElement extends NonElement<Type, Class>, RuntimeTypeInfo {
  <V> Transducer<V> getTransducer();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeNonElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */