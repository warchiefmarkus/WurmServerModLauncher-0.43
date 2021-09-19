package 1.0.org.apache.xml.resolver.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.xml.sax.AttributeList;
import org.xml.sax.DTDHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

public class ResolvingParser implements Parser, DTDHandler, DocumentHandler, EntityResolver {
  public static boolean namespaceAware = true;
  
  public static boolean validating = false;
  
  public static boolean suppressExplanation = false;
  
  private SAXParser saxParser = null;
  
  private Parser parser = null;
  
  private DocumentHandler documentHandler = null;
  
  private DTDHandler dtdHandler = null;
  
  private CatalogResolver catalogResolver = new CatalogResolver();
  
  private CatalogResolver piCatalogResolver = null;
  
  private boolean allowXMLCatalogPI = false;
  
  private boolean oasisXMLCatalogPI = false;
  
  private URL baseURL = null;
  
  public ResolvingParser() {
    SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
    sAXParserFactory.setNamespaceAware(namespaceAware);
    sAXParserFactory.setValidating(validating);
    try {
      this.saxParser = sAXParserFactory.newSAXParser();
      this.parser = this.saxParser.getParser();
      this.documentHandler = null;
      this.dtdHandler = null;
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
  
  public Catalog getCatalog() {
    return this.catalogResolver.getCatalog();
  }
  
  public void parse(InputSource paramInputSource) throws IOException, SAXException {
    setupParse(paramInputSource.getSystemId());
    try {
      this.parser.parse(paramInputSource);
    } catch (InternalError internalError) {
      explain(paramInputSource.getSystemId());
      throw internalError;
    } 
  }
  
  public void parse(String paramString) throws IOException, SAXException {
    setupParse(paramString);
    try {
      this.parser.parse(paramString);
    } catch (InternalError internalError) {
      explain(paramString);
      throw internalError;
    } 
  }
  
  public void setDocumentHandler(DocumentHandler paramDocumentHandler) {
    this.documentHandler = paramDocumentHandler;
  }
  
  public void setDTDHandler(DTDHandler paramDTDHandler) {
    this.dtdHandler = paramDTDHandler;
  }
  
  public void setEntityResolver(EntityResolver paramEntityResolver) {}
  
  public void setErrorHandler(ErrorHandler paramErrorHandler) {
    this.parser.setErrorHandler(paramErrorHandler);
  }
  
  public void setLocale(Locale paramLocale) throws SAXException {
    this.parser.setLocale(paramLocale);
  }
  
  public void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {
    if (this.documentHandler != null)
      this.documentHandler.characters(paramArrayOfchar, paramInt1, paramInt2); 
  }
  
  public void endDocument() throws SAXException {
    if (this.documentHandler != null)
      this.documentHandler.endDocument(); 
  }
  
  public void endElement(String paramString) throws SAXException {
    if (this.documentHandler != null)
      this.documentHandler.endElement(paramString); 
  }
  
  public void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {
    if (this.documentHandler != null)
      this.documentHandler.ignorableWhitespace(paramArrayOfchar, paramInt1, paramInt2); 
  }
  
  public void processingInstruction(String paramString1, String paramString2) throws SAXException {
    if (paramString1.equals("oasis-xml-catalog")) {
      URL uRL = null;
      String str = paramString2;
      int i = str.indexOf("catalog=");
      if (i >= 0) {
        str = str.substring(i + 8);
        if (str.length() > 1) {
          String str1 = str.substring(0, 1);
          str = str.substring(1);
          i = str.indexOf(str1);
          if (i >= 0) {
            str = str.substring(0, i);
            try {
              if (this.baseURL != null) {
                uRL = new URL(this.baseURL, str);
              } else {
                uRL = new URL(str);
              } 
            } catch (MalformedURLException malformedURLException) {}
          } 
        } 
      } 
      if (this.allowXMLCatalogPI) {
        if (CatalogManager.allowOasisXMLCatalogPI()) {
          Debug.message(4, "oasis-xml-catalog PI", paramString2);
          if (uRL != null) {
            try {
              Debug.message(4, "oasis-xml-catalog", uRL.toString());
              this.oasisXMLCatalogPI = true;
              if (this.piCatalogResolver == null)
                this.piCatalogResolver = new CatalogResolver(true); 
              this.piCatalogResolver.getCatalog().parseCatalog(uRL.toString());
            } catch (Exception exception) {
              Debug.message(3, "Exception parsing oasis-xml-catalog: " + uRL.toString());
            } 
          } else {
            Debug.message(3, "PI oasis-xml-catalog unparseable: " + paramString2);
          } 
        } else {
          Debug.message(4, "PI oasis-xml-catalog ignored: " + paramString2);
        } 
      } else {
        Debug.message(3, "PI oasis-xml-catalog occurred in an invalid place: " + paramString2);
      } 
    } else if (this.documentHandler != null) {
      this.documentHandler.processingInstruction(paramString1, paramString2);
    } 
  }
  
  public void setDocumentLocator(Locator paramLocator) {
    if (this.documentHandler != null)
      this.documentHandler.setDocumentLocator(paramLocator); 
  }
  
  public void startDocument() throws SAXException {
    if (this.documentHandler != null)
      this.documentHandler.startDocument(); 
  }
  
  public void startElement(String paramString, AttributeList paramAttributeList) throws SAXException {
    this.allowXMLCatalogPI = false;
    if (this.documentHandler != null)
      this.documentHandler.startElement(paramString, paramAttributeList); 
  }
  
  public void notationDecl(String paramString1, String paramString2, String paramString3) throws SAXException {
    this.allowXMLCatalogPI = false;
    if (this.dtdHandler != null)
      this.dtdHandler.notationDecl(paramString1, paramString2, paramString3); 
  }
  
  public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4) throws SAXException {
    this.allowXMLCatalogPI = false;
    if (this.dtdHandler != null)
      this.dtdHandler.unparsedEntityDecl(paramString1, paramString2, paramString3, paramString4); 
  }
  
  public InputSource resolveEntity(String paramString1, String paramString2) {
    this.allowXMLCatalogPI = false;
    String str = this.catalogResolver.getResolvedEntity(paramString1, paramString2);
    if (str == null && this.piCatalogResolver != null)
      str = this.piCatalogResolver.getResolvedEntity(paramString1, paramString2); 
    if (str != null)
      try {
        InputSource inputSource = new InputSource(str);
        inputSource.setPublicId(paramString1);
        URL uRL = new URL(str);
        InputStream inputStream = uRL.openStream();
        inputSource.setByteStream(inputStream);
        return inputSource;
      } catch (Exception exception) {
        Debug.message(1, "Failed to create InputSource", str);
        return null;
      }  
    return null;
  }
  
  private void setupParse(String paramString) {
    this.allowXMLCatalogPI = true;
    this.parser.setEntityResolver(this);
    this.parser.setDocumentHandler(this);
    this.parser.setDTDHandler(this);
    String str = System.getProperty("user.dir");
    URL uRL = null;
    str.replace('\\', '/');
    try {
      uRL = new URL("file:///" + str + "/basename");
    } catch (MalformedURLException malformedURLException) {
      uRL = null;
    } 
    try {
      this.baseURL = new URL(paramString);
    } catch (MalformedURLException malformedURLException) {
      if (uRL != null) {
        try {
          this.baseURL = new URL(uRL, paramString);
        } catch (MalformedURLException malformedURLException1) {
          this.baseURL = null;
        } 
      } else {
        this.baseURL = null;
      } 
    } 
  }
  
  private void explain(String paramString) {
    if (!suppressExplanation) {
      System.out.println("Parser probably encountered bad URI in " + paramString);
      System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
    } 
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\tools\ResolvingParser.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */