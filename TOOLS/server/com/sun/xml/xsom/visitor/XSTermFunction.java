package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSWildcard;

public interface XSTermFunction<T> {
  T wildcard(XSWildcard paramXSWildcard);
  
  T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);
  
  T modelGroup(XSModelGroup paramXSModelGroup);
  
  T elementDecl(XSElementDecl paramXSElementDecl);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSTermFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */