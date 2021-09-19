package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSUnionSimpleType;

public interface XSSimpleTypeFunction<T> {
  T listSimpleType(XSListSimpleType paramXSListSimpleType);
  
  T unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);
  
  T restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSSimpleTypeFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */