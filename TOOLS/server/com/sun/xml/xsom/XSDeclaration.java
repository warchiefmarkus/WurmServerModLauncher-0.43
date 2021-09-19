package com.sun.xml.xsom;

public interface XSDeclaration extends XSComponent {
  String getTargetNamespace();
  
  String getName();
  
  boolean isAnonymous();
  
  boolean isGlobal();
  
  boolean isLocal();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */