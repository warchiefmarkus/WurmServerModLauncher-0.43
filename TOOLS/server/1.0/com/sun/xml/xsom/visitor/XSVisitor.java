package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSNotation;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
import com.sun.xml.xsom.visitor.XSTermVisitor;

public interface XSVisitor extends XSTermVisitor, XSContentTypeVisitor {
  void annotation(XSAnnotation paramXSAnnotation);
  
  void attGroupDecl(XSAttGroupDecl paramXSAttGroupDecl);
  
  void attributeDecl(XSAttributeDecl paramXSAttributeDecl);
  
  void attributeUse(XSAttributeUse paramXSAttributeUse);
  
  void complexType(XSComplexType paramXSComplexType);
  
  void schema(XSSchema paramXSSchema);
  
  void facet(XSFacet paramXSFacet);
  
  void notation(XSNotation paramXSNotation);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSVisitor.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */