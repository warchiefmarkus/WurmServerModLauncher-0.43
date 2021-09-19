package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSSimpleType;

public interface XSUnionSimpleType extends XSSimpleType {
  XSSimpleType getMember(int paramInt);
  
  int getMemberSize();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSUnionSimpleType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */