package com.sun.xml.bind.v2.model.core;

import java.util.List;

public interface ClassInfo<T, C> extends MaybeElement<T, C> {
  ClassInfo<T, C> getBaseClass();
  
  C getClazz();
  
  String getName();
  
  List<? extends PropertyInfo<T, C>> getProperties();
  
  boolean hasValueProperty();
  
  PropertyInfo<T, C> getProperty(String paramString);
  
  boolean hasProperties();
  
  boolean isAbstract();
  
  boolean isOrdered();
  
  boolean isFinal();
  
  boolean hasSubClasses();
  
  boolean hasAttributeWildcard();
  
  boolean inheritsAttributeWildcard();
  
  boolean declaresAttributeWildcard();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\ClassInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */