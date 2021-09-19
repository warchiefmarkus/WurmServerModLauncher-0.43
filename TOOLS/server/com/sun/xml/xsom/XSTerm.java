package com.sun.xml.xsom;

import com.sun.xml.xsom.visitor.XSTermFunction;
import com.sun.xml.xsom.visitor.XSTermFunctionWithParam;
import com.sun.xml.xsom.visitor.XSTermVisitor;

public interface XSTerm extends XSComponent {
  void visit(XSTermVisitor paramXSTermVisitor);
  
  <T> T apply(XSTermFunction<T> paramXSTermFunction);
  
  <T, P> T apply(XSTermFunctionWithParam<T, P> paramXSTermFunctionWithParam, P paramP);
  
  boolean isWildcard();
  
  boolean isModelGroupDecl();
  
  boolean isModelGroup();
  
  boolean isElementDecl();
  
  XSWildcard asWildcard();
  
  XSModelGroupDecl asModelGroupDecl();
  
  XSModelGroup asModelGroup();
  
  XSElementDecl asElementDecl();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSTerm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */