package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public interface BIDeclaration {
  void setParent(BindInfo paramBindInfo);
  
  QName getName();
  
  Locator getLocation();
  
  void markAsAcknowledged();
  
  boolean isAcknowledged();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIDeclaration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */