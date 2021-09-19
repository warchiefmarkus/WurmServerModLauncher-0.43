package 1.0.org.apache.xml.resolver.readers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.readers.CatalogReader;
import org.apache.xml.resolver.readers.SAXCatalogParser;
import org.apache.xml.resolver.readers.SAXParserHandler;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXCatalogReader implements CatalogReader, ContentHandler, DocumentHandler {
  protected SAXParserFactory parserFactory = null;
  
  protected String parserClass = null;
  
  protected Hashtable namespaceMap = new Hashtable();
  
  private SAXCatalogParser saxParser = null;
  
  private boolean abandonHope = false;
  
  private Catalog catalog;
  
  public void setParserFactory(SAXParserFactory paramSAXParserFactory) {
    this.parserFactory = paramSAXParserFactory;
  }
  
  public void setParserClass(String paramString) {
    this.parserClass = paramString;
  }
  
  public SAXParserFactory getParserFactory() {
    return this.parserFactory;
  }
  
  public String getParserClass() {
    return this.parserClass;
  }
  
  public void setCatalogParser(String paramString1, String paramString2, String paramString3) {
    if (paramString1 == null) {
      this.namespaceMap.put(paramString2, paramString3);
    } else {
      this.namespaceMap.put("{" + paramString1 + "}" + paramString2, paramString3);
    } 
  }
  
  public String getCatalogParser(String paramString1, String paramString2) {
    return (paramString1 == null) ? (String)this.namespaceMap.get(paramString2) : (String)this.namespaceMap.get("{" + paramString1 + "}" + paramString2);
  }
  
  public SAXCatalogReader() {
    this.parserFactory = null;
    this.parserClass = null;
  }
  
  public SAXCatalogReader(SAXParserFactory paramSAXParserFactory) {
    this.parserFactory = paramSAXParserFactory;
  }
  
  public SAXCatalogReader(String paramString) {
    this.parserClass = paramString;
  }
  
  public void readCatalog(Catalog paramCatalog, String paramString) throws MalformedURLException, IOException, CatalogException {
    URL uRL = null;
    try {
      uRL = new URL(paramString);
    } catch (MalformedURLException malformedURLException) {
      uRL = new URL("file:///" + paramString);
    } 
    try {
      URLConnection uRLConnection = uRL.openConnection();
      readCatalog(paramCatalog, uRLConnection.getInputStream());
    } catch (FileNotFoundException fileNotFoundException) {
      Debug.message(1, "Failed to load catalog, file not found", uRL.toString());
    } 
  }
  
  public void readCatalog(Catalog paramCatalog, InputStream paramInputStream) throws IOException, CatalogException {
    if (this.parserFactory == null && this.parserClass == null) {
      Debug.message(1, "Cannot read SAX catalog without a parser");
      throw new CatalogException(6);
    } 
    this.catalog = paramCatalog;
    try {
      if (this.parserFactory != null) {
        SAXParser sAXParser = this.parserFactory.newSAXParser();
        SAXParserHandler sAXParserHandler = new SAXParserHandler();
        sAXParserHandler.setContentHandler(this);
        sAXParser.parse(new InputSource(paramInputStream), (DefaultHandler)sAXParserHandler);
      } else {
        Parser parser = (Parser)Class.forName(this.parserClass).newInstance();
        parser.setDocumentHandler(this);
        parser.parse(new InputSource(paramInputStream));
      } 
    } catch (ClassNotFoundException classNotFoundException) {
      throw new CatalogException(6);
    } catch (IllegalAccessException illegalAccessException) {
      throw new CatalogException(6);
    } catch (InstantiationException instantiationException) {
      throw new CatalogException(6);
    } catch (ParserConfigurationException parserConfigurationException) {
      throw new CatalogException(5);
    } catch (SAXException sAXException) {
      Exception exception = sAXException.getException();
      UnknownHostException unknownHostException = new UnknownHostException();
      FileNotFoundException fileNotFoundException = new FileNotFoundException();
      if (exception != null) {
        if (exception.getClass() == unknownHostException.getClass())
          throw new CatalogException(7, exception.toString()); 
        if (exception.getClass() == fileNotFoundException.getClass())
          throw new CatalogException(7, exception.toString()); 
      } 
      throw new CatalogException(sAXException);
    } 
  }
  
  public void setDocumentLocator(Locator paramLocator) {
    if (this.saxParser != null)
      this.saxParser.setDocumentLocator(paramLocator); 
  }
  
  public void startDocument() throws SAXException {
    this.saxParser = null;
    this.abandonHope = false;
  }
  
  public void endDocument() throws SAXException {
    if (this.saxParser != null)
      this.saxParser.endDocument(); 
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList) throws SAXException {
    if (this.abandonHope)
      return; 
    if (this.saxParser == null) {
      String str1 = "";
      if (paramString.indexOf(':') > 0)
        str1 = paramString.substring(0, paramString.indexOf(':')); 
      String str2 = paramString;
      if (str2.indexOf(':') > 0)
        str2 = str2.substring(str2.indexOf(':') + 1); 
      String str3 = null;
      if (str1.equals("")) {
        str3 = paramAttributeList.getValue("xmlns");
      } else {
        str3 = paramAttributeList.getValue("xmlns:" + str1);
      } 
      String str4 = getCatalogParser(str3, str2);
      if (str4 == null) {
        this.abandonHope = true;
        if (str3 == null) {
          Debug.message(2, "No Catalog parser for " + paramString);
        } else {
          Debug.message(2, "No Catalog parser for {" + str3 + "}" + paramString);
        } 
        return;
      } 
      try {
        this.saxParser = (SAXCatalogParser)Class.forName(str4).newInstance();
        this.saxParser.setCatalog(this.catalog);
        this.saxParser.startDocument();
        this.saxParser.startElement(paramString, paramAttributeList);
      } catch (ClassNotFoundException classNotFoundException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, classNotFoundException.toString());
      } catch (InstantiationException instantiationException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, instantiationException.toString());
      } catch (IllegalAccessException illegalAccessException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, illegalAccessException.toString());
      } catch (ClassCastException classCastException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, classCastException.toString());
      } 
    } else {
      this.saxParser.startElement(paramString, paramAttributeList);
    } 
  }
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException {
    if (this.abandonHope)
      return; 
    if (this.saxParser == null) {
      String str = getCatalogParser(paramString1, paramString2);
      if (str == null) {
        this.abandonHope = true;
        if (paramString1 == null) {
          Debug.message(2, "No Catalog parser for " + paramString2);
        } else {
          Debug.message(2, "No Catalog parser for {" + paramString1 + "}" + paramString2);
        } 
        return;
      } 
      try {
        this.saxParser = (SAXCatalogParser)Class.forName(str).newInstance();
        this.saxParser.setCatalog(this.catalog);
        this.saxParser.startDocument();
        this.saxParser.startElement(paramString1, paramString2, paramString3, paramAttributes);
      } catch (ClassNotFoundException classNotFoundException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, classNotFoundException.toString());
      } catch (InstantiationException instantiationException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, instantiationException.toString());
      } catch (IllegalAccessException illegalAccessException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, illegalAccessException.toString());
      } catch (ClassCastException classCastException) {
        this.saxParser = null;
        this.abandonHope = true;
        Debug.message(2, classCastException.toString());
      } 
    } else {
      this.saxParser.startElement(paramString1, paramString2, paramString3, paramAttributes);
    } 
  }
  
  public void endElement(String paramString) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.endElement(paramString); 
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.endElement(paramString1, paramString2, paramString3); 
  }
  
  public void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.characters(paramArrayOfchar, paramInt1, paramInt2); 
  }
  
  public void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.ignorableWhitespace(paramArrayOfchar, paramInt1, paramInt2); 
  }
  
  public void processingInstruction(String paramString1, String paramString2) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.processingInstruction(paramString1, paramString2); 
  }
  
  public void startPrefixMapping(String paramString1, String paramString2) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.startPrefixMapping(paramString1, paramString2); 
  }
  
  public void endPrefixMapping(String paramString) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.endPrefixMapping(paramString); 
  }
  
  public void skippedEntity(String paramString) throws SAXException {
    if (this.saxParser != null)
      this.saxParser.skippedEntity(paramString); 
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\SAXCatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */