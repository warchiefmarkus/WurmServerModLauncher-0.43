package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSDeclaration;
import com.sun.xml.xsom.XSSimpleType;
import org.relaxng.datatype.ValidationContext;

public interface XSAttributeDecl extends XSDeclaration {
  XSSimpleType getType();
  
  String getDefaultValue();
  
  String getFixedValue();
  
  ValidationContext getContext();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSAttributeDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */