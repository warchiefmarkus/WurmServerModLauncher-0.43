package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSWildcard;

public interface XSTermVisitor {
  void wildcard(XSWildcard paramXSWildcard);
  
  void modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);
  
  void modelGroup(XSModelGroup paramXSModelGroup);
  
  void elementDecl(XSElementDecl paramXSElementDecl);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSTermVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */