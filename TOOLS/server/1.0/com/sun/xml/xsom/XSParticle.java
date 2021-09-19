package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSTerm;

public interface XSParticle extends XSContentType {
  public static final int UNBOUNDED = -1;
  
  int getMinOccurs();
  
  int getMaxOccurs();
  
  XSTerm getTerm();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSParticle.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */