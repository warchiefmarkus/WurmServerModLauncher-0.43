package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import java.util.Iterator;

public interface XSSchema extends XSComponent {
  String getTargetNamespace();
  
  Iterator iterateAttributeDecls();
  
  XSAttributeDecl getAttributeDecl(String paramString);
  
  Iterator iterateElementDecls();
  
  XSElementDecl getElementDecl(String paramString);
  
  Iterator iterateAttGroupDecls();
  
  XSAttGroupDecl getAttGroupDecl(String paramString);
  
  Iterator iterateModelGroupDecls();
  
  XSModelGroupDecl getModelGroupDecl(String paramString);
  
  Iterator iterateTypes();
  
  XSType getType(String paramString);
  
  Iterator iterateSimpleTypes();
  
  XSSimpleType getSimpleType(String paramString);
  
  Iterator iterateComplexTypes();
  
  XSComplexType getComplexType(String paramString);
  
  Iterator iterateNotations();
  
  XSNotation getNotation(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSSchema.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */