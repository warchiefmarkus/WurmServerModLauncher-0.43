package 1.0.org.apache.xml.resolver.readers;

import java.util.Vector;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.helpers.PublicId;
import org.apache.xml.resolver.readers.SAXCatalogParser;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
  protected Catalog catalog = null;
  
  public void setCatalog(Catalog paramCatalog) {
    this.catalog = paramCatalog;
  }
  
  public Catalog getCatalog() {
    return this.catalog;
  }
  
  public XCatalogReader(SAXParserFactory paramSAXParserFactory) {
    super(paramSAXParserFactory);
  }
  
  public void setDocumentLocator(Locator paramLocator) {}
  
  public void startDocument() throws SAXException {}
  
  public void endDocument() throws SAXException {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException {
    int i = -1;
    Vector vector = new Vector();
    if (paramString2.equals("Base")) {
      this;
      i = Catalog.BASE;
      vector.add(paramAttributes.getValue("HRef"));
      Debug.message(4, "Base", paramAttributes.getValue("HRef"));
    } else if (paramString2.equals("Delegate")) {
      this;
      i = Catalog.DELEGATE_PUBLIC;
      vector.add(paramAttributes.getValue("PublicId"));
      vector.add(paramAttributes.getValue("HRef"));
      Debug.message(4, "Delegate", PublicId.normalize(paramAttributes.getValue("PublicId")), paramAttributes.getValue("HRef"));
    } else if (paramString2.equals("Extend")) {
      this;
      i = Catalog.CATALOG;
      vector.add(paramAttributes.getValue("HRef"));
      Debug.message(4, "Extend", paramAttributes.getValue("HRef"));
    } else if (paramString2.equals("Map")) {
      this;
      i = Catalog.PUBLIC;
      vector.add(paramAttributes.getValue("PublicId"));
      vector.add(paramAttributes.getValue("HRef"));
      Debug.message(4, "Map", PublicId.normalize(paramAttributes.getValue("PublicId")), paramAttributes.getValue("HRef"));
    } else if (paramString2.equals("Remap")) {
      this;
      i = Catalog.SYSTEM;
      vector.add(paramAttributes.getValue("SystemId"));
      vector.add(paramAttributes.getValue("HRef"));
      Debug.message(4, "Remap", paramAttributes.getValue("SystemId"), paramAttributes.getValue("HRef"));
    } else if (!paramString2.equals("XMLCatalog")) {
      Debug.message(1, "Invalid catalog entry type", paramString2);
    } 
    if (i >= 0)
      try {
        CatalogEntry catalogEntry = new CatalogEntry(i, vector);
        this.catalog.addEntry(catalogEntry);
      } catch (CatalogException catalogException) {
        if (catalogException.getExceptionType() == 3) {
          Debug.message(1, "Invalid catalog entry type", paramString2);
        } else if (catalogException.getExceptionType() == 2) {
          Debug.message(1, "Invalid catalog entry", paramString2);
        } 
      }  
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {}
  
  public void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {}
  
  public void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {}
  
  public void processingInstruction(String paramString1, String paramString2) throws SAXException {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\XCatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */