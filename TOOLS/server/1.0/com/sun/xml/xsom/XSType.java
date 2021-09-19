package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSDeclaration;
import com.sun.xml.xsom.XSSimpleType;

public interface XSType extends XSDeclaration {
  public static final int EXTENSION = 1;
  
  public static final int RESTRICTION = 2;
  
  public static final int SUBSTITUTION = 4;
  
  com.sun.xml.xsom.XSType getBaseType();
  
  int getDerivationMethod();
  
  boolean isSimpleType();
  
  boolean isComplexType();
  
  com.sun.xml.xsom.XSType[] listSubstitutables();
  
  XSSimpleType asSimpleType();
  
  XSComplexType asComplexType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */