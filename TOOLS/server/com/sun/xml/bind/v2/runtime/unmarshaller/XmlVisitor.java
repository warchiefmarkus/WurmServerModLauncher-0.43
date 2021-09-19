package com.sun.xml.bind.v2.runtime.unmarshaller;

import javax.xml.namespace.NamespaceContext;
import org.xml.sax.SAXException;

public interface XmlVisitor {
  void startDocument(LocatorEx paramLocatorEx, NamespaceContext paramNamespaceContext) throws SAXException;
  
  void endDocument() throws SAXException;
  
  void startElement(TagName paramTagName) throws SAXException;
  
  void endElement(TagName paramTagName) throws SAXException;
  
  void startPrefixMapping(String paramString1, String paramString2) throws SAXException;
  
  void endPrefixMapping(String paramString) throws SAXException;
  
  void text(CharSequence paramCharSequence) throws SAXException;
  
  UnmarshallingContext getContext();
  
  TextPredictor getPredictor();
  
  public static interface TextPredictor {
    boolean expectText();
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtim\\unmarshaller\XmlVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */