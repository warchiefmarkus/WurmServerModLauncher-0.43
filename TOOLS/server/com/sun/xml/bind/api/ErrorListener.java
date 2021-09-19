package com.sun.xml.bind.api;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public interface ErrorListener extends ErrorHandler {
  void error(SAXParseException paramSAXParseException);
  
  void fatalError(SAXParseException paramSAXParseException);
  
  void warning(SAXParseException paramSAXParseException);
  
  void info(SAXParseException paramSAXParseException);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\ErrorListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */