package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;

public interface XSContentTypeVisitor {
  void simpleType(XSSimpleType paramXSSimpleType);
  
  void particle(XSParticle paramXSParticle);
  
  void empty(XSContentType paramXSContentType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSContentTypeVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */