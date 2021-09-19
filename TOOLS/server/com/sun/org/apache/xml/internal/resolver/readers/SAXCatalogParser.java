package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

public interface SAXCatalogParser extends ContentHandler, DocumentHandler {
  void setCatalog(Catalog paramCatalog);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\SAXCatalogParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */