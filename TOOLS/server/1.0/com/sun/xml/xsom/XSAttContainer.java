package 1.0.com.sun.xml.xsom;

import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSDeclaration;
import com.sun.xml.xsom.XSWildcard;
import java.util.Iterator;

public interface XSAttContainer extends XSDeclaration {
  XSWildcard getAttributeWildcard();
  
  XSAttributeUse getAttributeUse(String paramString1, String paramString2);
  
  Iterator iterateAttributeUses();
  
  XSAttributeUse getDeclaredAttributeUse(String paramString1, String paramString2);
  
  Iterator iterateDeclaredAttributeUses();
  
  Iterator iterateAttGroups();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\XSAttContainer.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */