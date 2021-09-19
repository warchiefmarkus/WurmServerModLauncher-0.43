package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.XSUnionSimpleType;
import com.sun.xml.xsom.XSVariety;
import com.sun.xml.xsom.visitor.XSSimpleTypeFunction;
import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;

public interface XSSimpleType extends XSType, XSContentType {
  com.sun.xml.xsom.XSSimpleType getSimpleBaseType();
  
  XSVariety getVariety();
  
  XSFacet getFacet(String paramString);
  
  void visit(XSSimpleTypeVisitor paramXSSimpleTypeVisitor);
  
  Object apply(XSSimpleTypeFunction paramXSSimpleTypeFunction);
  
  boolean isRestriction();
  
  boolean isList();
  
  boolean isUnion();
  
  XSRestrictionSimpleType asRestriction();
  
  XSListSimpleType asList();
  
  XSUnionSimpleType asUnion();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSSimpleType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */