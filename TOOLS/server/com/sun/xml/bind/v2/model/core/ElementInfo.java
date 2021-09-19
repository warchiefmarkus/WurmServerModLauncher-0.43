package com.sun.xml.bind.v2.model.core;

import java.util.Collection;

public interface ElementInfo<T, C> extends Element<T, C> {
  ElementPropertyInfo<T, C> getProperty();
  
  NonElement<T, C> getContentType();
  
  T getContentInMemoryType();
  
  T getType();
  
  ElementInfo<T, C> getSubstitutionHead();
  
  Collection<? extends ElementInfo<T, C>> getSubstitutionMembers();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\ElementInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */