package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.visitor.XSFunction;
import com.sun.xml.xsom.visitor.XSVisitor;
import org.xml.sax.Locator;

public interface XSComponent {
  XSAnnotation getAnnotation();
  
  Locator getLocator();
  
  XSSchema getOwnerSchema();
  
  void visit(XSVisitor paramXSVisitor);
  
  Object apply(XSFunction paramXSFunction);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSComponent.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */