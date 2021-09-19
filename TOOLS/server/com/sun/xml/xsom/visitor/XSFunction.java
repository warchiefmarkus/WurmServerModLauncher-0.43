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

public interface XSFunction<T> extends XSContentTypeFunction<T>, XSTermFunction<T> {
  T annotation(XSAnnotation paramXSAnnotation);
  
  T attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  T attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  T attributeUse(XSAttributeUse paramXSAttributeUse);
  
  T complexType(XSComplexType paramXSComplexType);
  
  T schema(XSSchema paramXSSchema);
  
  T facet(XSFacet paramXSFacet);
  
  T notation(XSNotation paramXSNotation);
  
  T identityConstraint(XSIdentityConstraint paramXSIdentityConstraint);
  
  T xpath(XSXPath paramXSXPath);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */