package com.sun.xml.xsom.impl.parser;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public interface PatcherManager {
  void addPatcher(Patch paramPatch);
  
  void addErrorChecker(Patch paramPatch);
  
  void reportError(String paramString, Locator paramLocator) throws SAXException;
  
  public static interface Patcher {
    void run() throws SAXException;
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\PatcherManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */