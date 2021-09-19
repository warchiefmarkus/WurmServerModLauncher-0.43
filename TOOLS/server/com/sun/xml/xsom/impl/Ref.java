package com.sun.xml.xsom.impl;

import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;

public abstract class Ref {
  public static interface IdentityConstraint {
    XSIdentityConstraint get();
  }
  
  public static interface Element extends Term {
    XSElementDecl get();
  }
  
  public static interface AttGroup {
    XSAttGroupDecl get();
  }
  
  public static interface Attribute {
    XSAttributeDecl getAttribute();
  }
  
  public static interface ComplexType extends Type {
    XSComplexType getType();
  }
  
  public static interface SimpleType extends Type {
    XSSimpleType getType();
  }
  
  public static interface ContentType {
    XSContentType getContentType();
  }
  
  public static interface Type {
    XSType getType();
  }
  
  public static interface Term {
    XSTerm getTerm();
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\Ref.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */