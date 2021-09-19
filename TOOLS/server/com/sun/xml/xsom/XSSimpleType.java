package com.sun.xml.xsom;

import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;

public interface XSSimpleType extends XSType, XSContentType {
  XSSimpleType getSimpleBaseType();
  
  XSVariety getVariety();
  
  XSSimpleType getPrimitiveType();
  
  boolean isPrimitive();
  
  XSListSimpleType getBaseListType();
  
  XSUnionSimpleType getBaseUnionType();
  
  boolean isFinal(XSVariety paramXSVariety);
  
  XSSimpleType getRedefinedBy();
  
  XSFacet getFacet(String paramString);
  
  void visit(XSSimpleTypeVisitor paramXSSimpleTypeVisitor);
  
  <T> T apply(XSSimpleTypeFunction<T> paramXSSimpleTypeFunction);
  
  boolean isRestriction();
  
  boolean isList();
  
  boolean isUnion();
  
  XSRestrictionSimpleType asRestriction();
  
  XSListSimpleType asList();
  
  XSUnionSimpleType asUnion();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSSimpleType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */