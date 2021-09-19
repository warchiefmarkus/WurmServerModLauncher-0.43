package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;

public interface XSContentTypeFunction {
  Object simpleType(XSSimpleType paramXSSimpleType);
  
  Object particle(XSParticle paramXSParticle);
  
  Object empty(XSContentType paramXSContentType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSContentTypeFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */