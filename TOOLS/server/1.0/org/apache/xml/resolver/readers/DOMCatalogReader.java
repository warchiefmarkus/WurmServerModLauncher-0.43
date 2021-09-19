package 1.0.org.apache.xml.resolver.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.helpers.Namespaces;
import org.apache.xml.resolver.readers.CatalogReader;
import org.apache.xml.resolver.readers.DOMCatalogParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class DOMCatalogReader implements CatalogReader {
  protected Hashtable namespaceMap = new Hashtable();
  
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
  
  public void readCatalog(Catalog paramCatalog, InputStream paramInputStream) throws IOException, CatalogException {
    DocumentBuilderFactory documentBuilderFactory = null;
    DocumentBuilder documentBuilder = null;
    documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(false);
    documentBuilderFactory.setValidating(false);
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException parserConfigurationException) {
      throw new CatalogException(6);
    } 
    Document document = null;
    try {
      document = documentBuilder.parse(paramInputStream);
    } catch (SAXException sAXException) {
      throw new CatalogException(5);
    } 
    Element element = document.getDocumentElement();
    String str1 = Namespaces.getNamespaceURI(element);
    String str2 = Namespaces.getLocalName(element);
    String str3 = getCatalogParser(str1, str2);
    if (str3 == null) {
      if (str1 == null) {
        Debug.message(1, "No Catalog parser for " + str2);
      } else {
        Debug.message(1, "No Catalog parser for {" + str1 + "}" + str2);
      } 
      return;
    } 
    DOMCatalogParser dOMCatalogParser = null;
    try {
      dOMCatalogParser = (DOMCatalogParser)Class.forName(str3).newInstance();
    } catch (ClassNotFoundException classNotFoundException) {
      Debug.message(1, "Cannot load XML Catalog Parser class", str3);
      throw new CatalogException(6);
    } catch (InstantiationException instantiationException) {
      Debug.message(1, "Cannot instantiate XML Catalog Parser class", str3);
      throw new CatalogException(6);
    } catch (IllegalAccessException illegalAccessException) {
      Debug.message(1, "Cannot access XML Catalog Parser class", str3);
      throw new CatalogException(6);
    } catch (ClassCastException classCastException) {
      Debug.message(1, "Cannot cast XML Catalog Parser class", str3);
      throw new CatalogException(6);
    } 
    for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling())
      dOMCatalogParser.parseCatalogEntry(paramCatalog, node); 
  }
  
  public void readCatalog(Catalog paramCatalog, String paramString) throws MalformedURLException, IOException, CatalogException {
    URL uRL = new URL(paramString);
    URLConnection uRLConnection = uRL.openConnection();
    readCatalog(paramCatalog, uRLConnection.getInputStream());
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\DOMCatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */