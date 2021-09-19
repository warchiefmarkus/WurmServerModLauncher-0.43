package com.sun.xml.xsom;

import com.sun.xml.xsom.visitor.XSContentTypeFunction;
import com.sun.xml.xsom.visitor.XSContentTypeVisitor;

public interface XSContentType extends XSComponent {
  XSSimpleType asSimpleType();
  
  XSParticle asParticle();
  
  XSContentType asEmpty();
  
  <T> T apply(XSContentTypeFunction<T> paramXSContentTypeFunction);
  
  void visit(XSContentTypeVisitor paramXSContentTypeVisitor);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSContentType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */