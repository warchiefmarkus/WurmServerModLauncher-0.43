package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSComponent;
import org.relaxng.datatype.ValidationContext;

public interface XSAttributeUse extends XSComponent {
  boolean isRequired();
  
  XSAttributeDecl getDecl();
  
  String getDefaultValue();
  
  String getFixedValue();
  
  ValidationContext getContext();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSAttributeUse.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */