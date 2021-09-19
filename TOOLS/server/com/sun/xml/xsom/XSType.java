package com.sun.xml.xsom;

public interface XSType extends XSDeclaration {
  public static final int EXTENSION = 1;
  
  public static final int RESTRICTION = 2;
  
  public static final int SUBSTITUTION = 4;
  
  XSType getBaseType();
  
  int getDerivationMethod();
  
  boolean isSimpleType();
  
  boolean isComplexType();
  
  XSType[] listSubstitutables();
  
  XSType getRedefinedBy();
  
  int getRedefinedCount();
  
  XSSimpleType asSimpleType();
  
  XSComplexType asComplexType();
  
  boolean isDerivedFrom(XSType paramXSType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */