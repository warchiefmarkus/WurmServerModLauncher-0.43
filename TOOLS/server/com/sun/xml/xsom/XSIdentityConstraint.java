package com.sun.xml.xsom;

import java.util.List;

public interface XSIdentityConstraint extends XSComponent {
  public static final short KEY = 0;
  
  public static final short KEYREF = 1;
  
  public static final short UNIQUE = 2;
  
  XSElementDecl getParent();
  
  String getName();
  
  String getTargetNamespace();
  
  short getCategory();
  
  XSXPath getSelector();
  
  List<XSXPath> getFields();
  
  XSIdentityConstraint getReferencedKey();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSIdentityConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */