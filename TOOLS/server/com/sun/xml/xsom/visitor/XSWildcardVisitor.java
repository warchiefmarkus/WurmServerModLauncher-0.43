package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSWildcard;

public interface XSWildcardVisitor {
  void any(XSWildcard.Any paramAny);
  
  void other(XSWildcard.Other paramOther);
  
  void union(XSWildcard.Union paramUnion);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSWildcardVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */