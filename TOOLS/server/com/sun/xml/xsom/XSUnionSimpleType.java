package com.sun.xml.xsom;

public interface XSUnionSimpleType extends XSSimpleType, Iterable<XSSimpleType> {
  XSSimpleType getMember(int paramInt);
  
  int getMemberSize();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSUnionSimpleType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */