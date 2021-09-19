package 1.0.org.apache.xml.resolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.readers.CatalogReader;
import org.apache.xml.resolver.readers.SAXCatalogReader;
import org.apache.xml.resolver.readers.TR9401CatalogReader;

public class Resolver extends Catalog {
  public static final int URISUFFIX = CatalogEntry.addEntryType("URISUFFIX", 2);
  
  public static final int SYSTEMSUFFIX = CatalogEntry.addEntryType("SYSTEMSUFFIX", 2);
  
  public static final int RESOLVER = CatalogEntry.addEntryType("RESOLVER", 1);
  
  public static final int SYSTEMREVERSE = CatalogEntry.addEntryType("SYSTEMREVERSE", 1);
  
  public void setupReaders() {
    SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
    sAXParserFactory.setNamespaceAware(true);
    sAXParserFactory.setValidating(false);
    SAXCatalogReader sAXCatalogReader = new SAXCatalogReader(sAXParserFactory);
    sAXCatalogReader.setCatalogParser(null, "XMLCatalog", "org.apache.xml.resolver.readers.XCatalogReader");
    sAXCatalogReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "org.apache.xml.resolver.readers.ExtendedXMLCatalogReader");
    addReader("application/xml", (CatalogReader)sAXCatalogReader);
    TR9401CatalogReader tR9401CatalogReader = new TR9401CatalogReader();
    addReader("text/plain", (CatalogReader)tR9401CatalogReader);
  }
  
  public void addEntry(CatalogEntry paramCatalogEntry) {
    int i = paramCatalogEntry.getEntryType();
    if (i == URISUFFIX) {
      String str1 = normalizeURI(paramCatalogEntry.getEntryArg(0));
      String str2 = makeAbsolute(normalizeURI(paramCatalogEntry.getEntryArg(1)));
      paramCatalogEntry.setEntryArg(1, str2);
      Debug.message(4, "URISUFFIX", str1, str2);
    } else if (i == SYSTEMSUFFIX) {
      String str1 = normalizeURI(paramCatalogEntry.getEntryArg(0));
      String str2 = makeAbsolute(normalizeURI(paramCatalogEntry.getEntryArg(1)));
      paramCatalogEntry.setEntryArg(1, str2);
      Debug.message(4, "SYSTEMSUFFIX", str1, str2);
    } 
    super.addEntry(paramCatalogEntry);
  }
  
  public String resolveURI(String paramString) throws MalformedURLException, IOException {
    String str = super.resolveURI(paramString);
    if (str != null)
      return str; 
    Enumeration enumeration = this.catalogEntries.elements();
    while (enumeration.hasMoreElements()) {
      CatalogEntry catalogEntry = enumeration.nextElement();
      if (catalogEntry.getEntryType() == RESOLVER) {
        str = resolveExternalSystem(paramString, catalogEntry.getEntryArg(0));
        if (str != null)
          return str; 
        continue;
      } 
      if (catalogEntry.getEntryType() == URISUFFIX) {
        String str1 = catalogEntry.getEntryArg(0);
        String str2 = catalogEntry.getEntryArg(1);
        if (str1.length() <= paramString.length() && paramString.substring(paramString.length() - str1.length()).equals(str1))
          return str2; 
      } 
    } 
    return resolveSubordinateCatalogs(Catalog.URI, null, null, paramString);
  }
  
  public String resolveSystem(String paramString) throws MalformedURLException, IOException {
    String str = super.resolveSystem(paramString);
    if (str != null)
      return str; 
    Enumeration enumeration = this.catalogEntries.elements();
    while (enumeration.hasMoreElements()) {
      CatalogEntry catalogEntry = enumeration.nextElement();
      if (catalogEntry.getEntryType() == RESOLVER) {
        str = resolveExternalSystem(paramString, catalogEntry.getEntryArg(0));
        if (str != null)
          return str; 
        continue;
      } 
      if (catalogEntry.getEntryType() == SYSTEMSUFFIX) {
        String str1 = catalogEntry.getEntryArg(0);
        String str2 = catalogEntry.getEntryArg(1);
        if (str1.length() <= paramString.length() && paramString.substring(paramString.length() - str1.length()).equals(str1))
          return str2; 
      } 
    } 
    return resolveSubordinateCatalogs(Catalog.SYSTEM, null, null, paramString);
  }
  
  public String resolvePublic(String paramString1, String paramString2) throws MalformedURLException, IOException {
    String str = super.resolvePublic(paramString1, paramString2);
    if (str != null)
      return str; 
    Enumeration enumeration = this.catalogEntries.elements();
    while (enumeration.hasMoreElements()) {
      CatalogEntry catalogEntry = enumeration.nextElement();
      if (catalogEntry.getEntryType() == RESOLVER) {
        if (paramString2 != null) {
          str = resolveExternalSystem(paramString2, catalogEntry.getEntryArg(0));
          if (str != null)
            return str; 
        } 
        str = resolveExternalPublic(paramString1, catalogEntry.getEntryArg(0));
        if (str != null)
          return str; 
      } 
    } 
    return resolveSubordinateCatalogs(Catalog.PUBLIC, null, paramString1, paramString2);
  }
  
  protected String resolveExternalSystem(String paramString1, String paramString2) throws MalformedURLException, IOException {
    org.apache.xml.resolver.Resolver resolver = queryResolver(paramString2, "i2l", paramString1, null);
    return (resolver != null) ? resolver.resolveSystem(paramString1) : null;
  }
  
  protected String resolveExternalPublic(String paramString1, String paramString2) throws MalformedURLException, IOException {
    org.apache.xml.resolver.Resolver resolver = queryResolver(paramString2, "fpi2l", paramString1, null);
    return (resolver != null) ? resolver.resolvePublic(paramString1, null) : null;
  }
  
  protected org.apache.xml.resolver.Resolver queryResolver(String paramString1, String paramString2, String paramString3, String paramString4) {
    Object object1 = null;
    String str = paramString1 + "?command=" + paramString2 + "&format=tr9401&uri=" + paramString3 + "&uri2=" + paramString4;
    Object object2 = null;
    try {
      URL uRL = new URL(str);
      URLConnection uRLConnection = uRL.openConnection();
      uRLConnection.setUseCaches(false);
      org.apache.xml.resolver.Resolver resolver = (org.apache.xml.resolver.Resolver)newCatalog();
      String str1 = uRLConnection.getContentType();
      if (str1.indexOf(";") > 0)
        str1 = str1.substring(0, str1.indexOf(";")); 
      resolver.parseCatalog(str1, uRLConnection.getInputStream());
      return resolver;
    } catch (CatalogException catalogException) {
      if (catalogException.getExceptionType() == 6) {
        Debug.message(1, "Unparseable catalog: " + str);
      } else if (catalogException.getExceptionType() == 5) {
        Debug.message(1, "Unknown catalog format: " + str);
      } 
      return null;
    } catch (MalformedURLException malformedURLException) {
      Debug.message(1, "Malformed resolver URL: " + str);
      return null;
    } catch (IOException iOException) {
      Debug.message(1, "I/O Exception opening resolver: " + str);
      return null;
    } 
  }
  
  private Vector appendVector(Vector paramVector1, Vector paramVector2) {
    if (paramVector2 != null)
      for (byte b = 0; b < paramVector2.size(); b++)
        paramVector1.addElement(paramVector2.elementAt(b));  
    return paramVector1;
  }
  
  public Vector resolveAllSystemReverse(String paramString) throws MalformedURLException, IOException {
    Vector vector1 = new Vector();
    if (paramString != null) {
      Vector vector = resolveLocalSystemReverse(paramString);
      vector1 = appendVector(vector1, vector);
    } 
    Vector vector2 = resolveAllSubordinateCatalogs(SYSTEMREVERSE, null, null, paramString);
    return appendVector(vector1, vector2);
  }
  
  public String resolveSystemReverse(String paramString) throws MalformedURLException, IOException {
    Vector vector = resolveAllSystemReverse(paramString);
    return (vector != null && vector.size() > 0) ? vector.elementAt(0) : null;
  }
  
  public Vector resolveAllSystem(String paramString) throws MalformedURLException, IOException {
    Vector vector1 = new Vector();
    if (paramString != null) {
      Vector vector = resolveAllLocalSystem(paramString);
      vector1 = appendVector(vector1, vector);
    } 
    Vector vector2 = resolveAllSubordinateCatalogs(Catalog.SYSTEM, null, null, paramString);
    vector1 = appendVector(vector1, vector2);
    return (vector1.size() > 0) ? vector1 : null;
  }
  
  private Vector resolveAllLocalSystem(String paramString) {
    Vector vector = new Vector();
    String str = System.getProperty("os.name");
    boolean bool = (str.indexOf("Windows") >= 0) ? true : false;
    Enumeration enumeration = this.catalogEntries.elements();
    while (enumeration.hasMoreElements()) {
      CatalogEntry catalogEntry = enumeration.nextElement();
      if (catalogEntry.getEntryType() == Catalog.SYSTEM && (catalogEntry.getEntryArg(0).equals(paramString) || (bool && catalogEntry.getEntryArg(0).equalsIgnoreCase(paramString))))
        vector.addElement(catalogEntry.getEntryArg(1)); 
    } 
    return (vector.size() == 0) ? null : vector;
  }
  
  private Vector resolveLocalSystemReverse(String paramString) {
    Vector vector = new Vector();
    String str = System.getProperty("os.name");
    boolean bool = (str.indexOf("Windows") >= 0) ? true : false;
    Enumeration enumeration = this.catalogEntries.elements();
    while (enumeration.hasMoreElements()) {
      CatalogEntry catalogEntry = enumeration.nextElement();
      if (catalogEntry.getEntryType() == Catalog.SYSTEM && (catalogEntry.getEntryArg(1).equals(paramString) || (bool && catalogEntry.getEntryArg(1).equalsIgnoreCase(paramString))))
        vector.addElement(catalogEntry.getEntryArg(0)); 
    } 
    return (vector.size() == 0) ? null : vector;
  }
  
  private synchronized Vector resolveAllSubordinateCatalogs(int paramInt, String paramString1, String paramString2, String paramString3) throws MalformedURLException, IOException {
    Vector vector = new Vector();
    for (byte b = 0; b < this.catalogs.size(); b++) {
      org.apache.xml.resolver.Resolver resolver = null;
      try {
        resolver = this.catalogs.elementAt(b);
      } catch (ClassCastException classCastException) {
        String str1 = this.catalogs.elementAt(b);
        resolver = (org.apache.xml.resolver.Resolver)newCatalog();
        try {
          resolver.parseCatalog(str1);
        } catch (MalformedURLException malformedURLException) {
          Debug.message(1, "Malformed Catalog URL", str1);
        } catch (FileNotFoundException fileNotFoundException) {
          Debug.message(1, "Failed to load catalog, file not found", str1);
        } catch (IOException iOException) {
          Debug.message(1, "Failed to load catalog, I/O error", str1);
        } 
        this.catalogs.setElementAt(resolver, b);
      } 
      String str = null;
      if (paramInt == Catalog.DOCTYPE) {
        str = resolver.resolveDoctype(paramString1, paramString2, paramString3);
        if (str != null) {
          vector.addElement(str);
          return vector;
        } 
      } else if (paramInt == Catalog.DOCUMENT) {
        str = resolver.resolveDocument();
        if (str != null) {
          vector.addElement(str);
          return vector;
        } 
      } else if (paramInt == Catalog.ENTITY) {
        str = resolver.resolveEntity(paramString1, paramString2, paramString3);
        if (str != null) {
          vector.addElement(str);
          return vector;
        } 
      } else if (paramInt == Catalog.NOTATION) {
        str = resolver.resolveNotation(paramString1, paramString2, paramString3);
        if (str != null) {
          vector.addElement(str);
          return vector;
        } 
      } else if (paramInt == Catalog.PUBLIC) {
        str = resolver.resolvePublic(paramString2, paramString3);
        if (str != null) {
          vector.addElement(str);
          return vector;
        } 
      } else {
        if (paramInt == Catalog.SYSTEM) {
          Vector vector1 = resolver.resolveAllSystem(paramString3);
          vector = appendVector(vector, vector1);
          break;
        } 
        if (paramInt == SYSTEMREVERSE) {
          Vector vector1 = resolver.resolveAllSystemReverse(paramString3);
          vector = appendVector(vector, vector1);
        } 
      } 
    } 
    return (vector != null) ? vector : null;
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\Resolver.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */