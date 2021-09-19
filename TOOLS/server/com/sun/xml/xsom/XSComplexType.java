package com.sun.xml.xsom;

public interface XSComplexType extends XSType, XSAttContainer {
  boolean isAbstract();
  
  boolean isFinal(int paramInt);
  
  boolean isSubstitutionProhibited(int paramInt);
  
  XSElementDecl getScope();
  
  XSContentType getContentType();
  
  XSContentType getExplicitContent();
  
  boolean isMixed();
  
  XSComplexType getRedefinedBy();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSComplexType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */