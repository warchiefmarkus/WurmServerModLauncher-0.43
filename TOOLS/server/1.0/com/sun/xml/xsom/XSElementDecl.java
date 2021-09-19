package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSDeclaration;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import java.util.Set;

public interface XSElementDecl extends XSDeclaration, XSTerm {
  XSType getType();
  
  boolean isNillable();
  
  com.sun.xml.xsom.XSElementDecl getSubstAffiliation();
  
  boolean isSubstitutionExcluded(int paramInt);
  
  boolean isSubstitutionDisallowed(int paramInt);
  
  boolean isAbstract();
  
  com.sun.xml.xsom.XSElementDecl[] listSubstitutables();
  
  Set getSubstitutables();
  
  boolean canBeSubstitutedBy(com.sun.xml.xsom.XSElementDecl paramXSElementDecl);
  
  String getDefaultValue();
  
  String getFixedValue();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSElementDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */