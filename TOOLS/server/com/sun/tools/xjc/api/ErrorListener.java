package com.sun.tools.xjc.api;

import com.sun.xml.bind.api.ErrorListener;
import org.xml.sax.SAXParseException;

public interface ErrorListener extends ErrorListener {
  void error(SAXParseException paramSAXParseException);
  
  void fatalError(SAXParseException paramSAXParseException);
  
  void warning(SAXParseException paramSAXParseException);
  
  void info(SAXParseException paramSAXParseException);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\ErrorListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */