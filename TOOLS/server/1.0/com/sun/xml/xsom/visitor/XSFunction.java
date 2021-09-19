package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.visitor.XSContentTypeFunction;
import com.sun.xml.xsom.visitor.XSTermFunction;

public interface XSFunction extends XSContentTypeFunction, XSTermFunction {
  Object annotation(XSAnnotation paramXSAnnotation);
  
  Object attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  Object attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  Object attributeUse(XSAttributeUse paramXSAttributeUse);
  
  Object complexType(XSComplexType paramXSComplexType);
  
  Object schema(XSSchema paramXSSchema);
  
  Object facet(XSFacet paramXSFacet);
  
  Object notation(XSNotation paramXSNotation);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */