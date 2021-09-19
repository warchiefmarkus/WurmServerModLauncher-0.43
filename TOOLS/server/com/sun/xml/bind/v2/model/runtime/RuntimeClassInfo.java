package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.ClassInfo;
import com.sun.xml.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public interface RuntimeClassInfo extends ClassInfo<Type, Class>, RuntimeNonElement {
  RuntimeClassInfo getBaseClass();
  
  List<? extends RuntimePropertyInfo> getProperties();
  
  RuntimePropertyInfo getProperty(String paramString);
  
  Method getFactoryMethod();
  
  <BeanT> Accessor<BeanT, Map<QName, String>> getAttributeWildcard();
  
  <BeanT> Accessor<BeanT, Locator> getLocatorField();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeClassInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */