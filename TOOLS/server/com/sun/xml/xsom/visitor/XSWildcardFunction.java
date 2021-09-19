package com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSWildcard;

public interface XSWildcardFunction<T> {
  T any(XSWildcard.Any paramAny);
  
  T other(XSWildcard.Other paramOther);
  
  T union(XSWildcard.Union paramUnion);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\visitor\XSWildcardFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */