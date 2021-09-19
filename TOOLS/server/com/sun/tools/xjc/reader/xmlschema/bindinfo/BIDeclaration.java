package com.sun.tools.xjc.reader.xmlschema.bindinfo;

import java.util.Collection;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public interface BIDeclaration {
  void setParent(BindInfo paramBindInfo);
  
  QName getName();
  
  Locator getLocation();
  
  void markAsAcknowledged();
  
  boolean isAcknowledged();
  
  void onSetOwner();
  
  Collection<BIDeclaration> getChildren();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\bindinfo\BIDeclaration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */