package 1.0.org.apache.xml.resolver.readers;

import org.apache.xml.resolver.Catalog;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;

public interface SAXCatalogParser extends ContentHandler, DocumentHandler {
  void setCatalog(Catalog paramCatalog);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\SAXCatalogParser.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */