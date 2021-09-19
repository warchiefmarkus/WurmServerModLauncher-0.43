package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSUnionSimpleType;

public interface XSSimpleTypeVisitor {
  void listSimpleType(XSListSimpleType paramXSListSimpleType);
  
  void unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);
  
  void restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSSimpleTypeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */