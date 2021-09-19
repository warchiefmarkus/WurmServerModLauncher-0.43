package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.LeafInfo;
import com.sun.xml.bind.v2.runtime.Transducer;
import java.lang.reflect.Type;
import javax.xml.namespace.QName;

public interface RuntimeLeafInfo extends LeafInfo<Type, Class>, RuntimeNonElement {
  <V> Transducer<V> getTransducer();
  
  Class getClazz();
  
  QName[] getTypeNames();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeLeafInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */