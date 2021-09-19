package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.visitor.XSContentTypeFunction;
import com.sun.xml.xsom.visitor.XSContentTypeVisitor;

public interface XSContentType extends XSComponent {
  XSSimpleType asSimpleType();
  
  XSParticle asParticle();
  
  com.sun.xml.xsom.XSContentType asEmpty();
  
  Object apply(XSContentTypeFunction paramXSContentTypeFunction);
  
  void visit(XSContentTypeVisitor paramXSContentTypeVisitor);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSContentType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */