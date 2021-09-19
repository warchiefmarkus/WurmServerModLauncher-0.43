package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;

public interface XSContentTypeFunction<T> {
  T simpleType(XSSimpleType paramXSSimpleType);
  
  T particle(XSParticle paramXSParticle);
  
  T empty(XSContentType paramXSContentType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSContentTypeFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */