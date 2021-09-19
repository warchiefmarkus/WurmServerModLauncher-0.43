package 1.0.com.sun.xml.xsom.visitor;

import com.sun.xml.xsom.XSWildcard;

public interface XSWildcardFunction {
  Object any(XSWildcard.Any paramAny);
  
  Object other(XSWildcard.Other paramOther);
  
  Object union(XSWildcard.Union paramUnion);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\visitor\XSWildcardFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */