package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSWildcard;

public interface XSTermFunctionWithParam<T, P> {
  T wildcard(XSWildcard paramXSWildcard, P paramP);
  
  T modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl, P paramP);
  
  T modelGroup(XSModelGroup paramXSModelGroup, P paramP);
  
  T elementDecl(XSElementDecl paramXSElementDecl, P paramP);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSTermFunctionWithParam.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */