package com.sun.xml.bind.v2.model.runtime;

import com.sun.xml.bind.v2.model.core.TypeInfoSet;
import com.sun.xml.bind.v2.model.nav.ReflectionNavigator;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import javax.xml.namespace.QName;

public interface RuntimeTypeInfoSet extends TypeInfoSet<Type, Class, Field, Method> {
  Map<Class, ? extends RuntimeArrayInfo> arrays();
  
  Map<Class, ? extends RuntimeClassInfo> beans();
  
  Map<Type, ? extends RuntimeBuiltinLeafInfo> builtins();
  
  Map<Class, ? extends RuntimeEnumLeafInfo> enums();
  
  RuntimeNonElement getTypeInfo(Type paramType);
  
  RuntimeNonElement getAnyTypeInfo();
  
  RuntimeNonElement getClassInfo(Class paramClass);
  
  RuntimeElementInfo getElementInfo(Class paramClass, QName paramQName);
  
  Map<QName, ? extends RuntimeElementInfo> getElementMappings(Class paramClass);
  
  Iterable<? extends RuntimeElementInfo> getAllElements();
  
  ReflectionNavigator getNavigator();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\runtime\RuntimeTypeInfoSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */