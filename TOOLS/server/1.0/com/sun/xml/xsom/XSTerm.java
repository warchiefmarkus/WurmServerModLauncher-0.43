package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSWildcard;
import com.sun.xml.xsom.visitor.XSTermFunction;
import com.sun.xml.xsom.visitor.XSTermVisitor;

public interface XSTerm extends XSComponent {
  void visit(XSTermVisitor paramXSTermVisitor);
  
  Object apply(XSTermFunction paramXSTermFunction);
  
  boolean isWildcard();
  
  boolean isModelGroupDecl();
  
  boolean isModelGroup();
  
  boolean isElementDecl();
  
  XSWildcard asWildcard();
  
  XSModelGroupDecl asModelGroupDecl();
  
  XSModelGroup asModelGroup();
  
  XSElementDecl asElementDecl();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSTerm.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */