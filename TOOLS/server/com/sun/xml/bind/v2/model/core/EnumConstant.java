package com.sun.xml.bind.v2.model.core;

public interface EnumConstant<T, C> {
  EnumLeafInfo<T, C> getEnclosingClass();
  
  String getLexicalValue();
  
  String getName();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\core\EnumConstant.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */