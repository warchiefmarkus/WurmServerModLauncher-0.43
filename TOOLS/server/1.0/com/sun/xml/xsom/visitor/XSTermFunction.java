package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSWildcard;

public interface XSTermFunction {
  Object wildcard(XSWildcard paramXSWildcard);
  
  Object modelGroupDecl(XSModelGroupDecl paramXSModelGroupDecl);
  
  Object modelGroup(XSModelGroup paramXSModelGroup);
  
  Object elementDecl(XSElementDecl paramXSElementDecl);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSTermFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */