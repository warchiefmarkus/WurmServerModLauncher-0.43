package com.sun.xml.bind.v2.model.impl;

import com.sun.xml.bind.v2.model.annotation.AnnotationSource;
import com.sun.xml.bind.v2.model.annotation.Locatable;

interface PropertySeed<T, C, F, M> extends Locatable, AnnotationSource {
  String getName();
  
  T getRawType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\impl\PropertySeed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */