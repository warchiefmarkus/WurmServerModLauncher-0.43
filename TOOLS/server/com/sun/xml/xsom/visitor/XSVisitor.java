package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSIdentityConstraint;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSXPath;

public interface XSVisitor extends XSTermVisitor, XSContentTypeVisitor {
  void annotation(XSAnnotation paramXSAnnotation);
  
  void attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  void attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  void attributeUse(XSAttributeUse paramXSAttributeUse);
  
  void complexType(XSComplexType paramXSComplexType);
  
  void schema(XSSchema paramXSSchema);
  
  void facet(XSFacet paramXSFacet);
  
  void notation(XSNotation paramXSNotation);
  
  void identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);
  
  void xpath(XSXPath paramXSXPath);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */