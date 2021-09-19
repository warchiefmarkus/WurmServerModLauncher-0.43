package com.sun.xml.xsom;

public interface XSAttributeUse extends XSComponent {
  boolean isRequired();
  
  XSAttributeDecl getDecl();
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSAttributeUse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */