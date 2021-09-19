package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAttContainer;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSType;

public interface XSComplexType extends XSType, XSAttContainer {
  boolean isAbstract();
  
  boolean isFinal(int paramInt);
  
  boolean isSubstitutionProhibited(int paramInt);
  
  XSElementDecl getScope();
  
  XSContentType getContentType();
  
  XSContentType getExplicitContent();
  
  boolean isMixed();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSComplexType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */