package com.sun.xml.xsom;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface XSRestrictionSimpleType extends XSSimpleType {
  Iterator<XSFacet> iterateDeclaredFacets();
  
  Collection<? extends XSFacet> getDeclaredFacets();
  
  XSFacet getDeclaredFacet(String paramString);
  
  List<XSFacet> getDeclaredFacets(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSRestrictionSimpleType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */