package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSListSimpleType;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSUnionSimpleType;

public interface XSSimpleTypeFunction {
  Object listSimpleType(XSListSimpleType paramXSListSimpleType);
  
  Object unionSimpleType(XSUnionSimpleType paramXSUnionSimpleType);
  
  Object restrictionSimpleType(XSRestrictionSimpleType paramXSRestrictionSimpleType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSSimpleTypeFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */