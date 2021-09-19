package com.sun.tools.xjc.model;

import com.sun.xml.xsom.XSComponent;
import org.xml.sax.Locator;

public interface CCustomizable {
  CCustomizations getCustomizations();
  
  Locator getLocator();
  
  XSComponent getSchemaComponent();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CCustomizable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */