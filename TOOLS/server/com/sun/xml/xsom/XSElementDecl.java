package com.sun.xml.xsom;

import java.util.List;
import java.util.Set;

public interface XSElementDecl extends XSDeclaration, XSTerm {
  XSType getType();
  
  boolean isNillable();
  
  XSElementDecl getSubstAffiliation();
  
  List<XSIdentityConstraint> getIdentityConstraints();
  
  boolean isSubstitutionExcluded(int paramInt);
  
  boolean isSubstitutionDisallowed(int paramInt);
  
  boolean isAbstract();
  
  XSElementDecl[] listSubstitutables();
  
  Set<? extends XSElementDecl> getSubstitutables();
  
  boolean canBeSubstitutedBy(XSElementDecl paramXSElementDecl);
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSElementDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */