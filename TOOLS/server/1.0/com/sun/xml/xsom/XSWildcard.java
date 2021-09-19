package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.visitor.XSWildcardFunction;
import com.sun.xml.xsom.visitor.XSWildcardVisitor;

public interface XSWildcard extends XSComponent, XSTerm {
  public static final int LAX = 1;
  
  public static final int STRTICT = 2;
  
  public static final int SKIP = 3;
  
  int getMode();
  
  boolean acceptsNamespace(String paramString);
  
  void visit(XSWildcardVisitor paramXSWildcardVisitor);
  
  Object apply(XSWildcardFunction paramXSWildcardFunction);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSWildcard.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */