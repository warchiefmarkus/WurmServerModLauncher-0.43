package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSSimpleType;
import java.util.Iterator;

public interface XSRestrictionSimpleType extends XSSimpleType {
  Iterator iterateDeclaredFacets();
  
  XSFacet getDeclaredFacet(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSRestrictionSimpleType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */