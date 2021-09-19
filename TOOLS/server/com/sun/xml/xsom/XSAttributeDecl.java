package com.sun.xml.xsom;

public interface XSAttributeDecl extends XSDeclaration {
  XSSimpleType getType();
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSAttributeDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */