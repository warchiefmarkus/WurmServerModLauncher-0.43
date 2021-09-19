package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroupDecl;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSimpleType;
import java.util.Iterator;

public interface XSSchemaSet {
  XSSchema getSchema(String paramString);
  
  XSSchema getSchema(int paramInt);
  
  int getSchemaSize();
  
  Iterator iterateSchema();
  
  XSSimpleType getSimpleType(String paramString1, String paramString2);
  
  XSAttributeDecl getAttributeDecl(String paramString1, String paramString2);
  
  XSElementDecl getElementDecl(String paramString1, String paramString2);
  
  XSModelGroupDecl getModelGroupDecl(String paramString1, String paramString2);
  
  XSAttGroupDecl getAttGroupDecl(String paramString1, String paramString2);
  
  XSComplexType getComplexType(String paramString1, String paramString2);
  
  Iterator iterateElementDecls();
  
  Iterator iterateTypes();
  
  Iterator iterateAttributeDecls();
  
  Iterator iterateAttGroupDecls();
  
  Iterator iterateModelGroupDecls();
  
  Iterator iterateSimpleTypes();
  
  Iterator iterateComplexTypes();
  
  Iterator iterateNotations();
  
  XSComplexType getAnyType();
  
  XSSimpleType getAnySimpleType();
  
  XSContentType getEmpty();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSSchemaSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */