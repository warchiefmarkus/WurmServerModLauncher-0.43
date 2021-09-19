package 1.0.org.apache.xml.resolver.readers;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
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

public class OASISXMLCatalogReader extends SAXCatalogReader implements SAXCatalogParser {
  protected Catalog catalog = null;
  
  public static final String namespaceName = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
  
  public static final String tr9401NamespaceName = "urn:oasis:names:tc:entity:xmlns:tr9401:catalog";
  
  protected Stack baseURIStack = new Stack();
  
  protected Stack overrideStack = new Stack();
  
  protected Stack namespaceStack = new Stack();
  
  public void setCatalog(Catalog paramCatalog) {
    this.catalog = paramCatalog;
  }
  
  public Catalog getCatalog() {
    return this.catalog;
  }
  
  protected boolean inExtensionNamespace() {
    boolean bool = false;
    Enumeration enumeration = this.namespaceStack.elements();
    while (!bool && enumeration.hasMoreElements()) {
      String str = enumeration.nextElement();
      if (str == null) {
        bool = true;
        continue;
      } 
      bool = (!str.equals("urn:oasis:names:tc:entity:xmlns:tr9401:catalog") && !str.equals("urn:oasis:names:tc:entity:xmlns:xml:catalog")) ? true : false;
    } 
    return bool;
  }
  
  public void setDocumentLocator(Locator paramLocator) {}
  
  public void startDocument() throws SAXException {
    this.baseURIStack.push(this.catalog.getCurrentBase());
    this.overrideStack.push(this.catalog.getDefaultOverride());
  }
  
  public void endDocument() throws SAXException {}
  
  public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException {
    int i = -1;
    Vector vector = new Vector();
    this.namespaceStack.push(paramString1);
    boolean bool = inExtensionNamespace();
    if (paramString1 != null && "urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(paramString1) && !bool) {
      if (paramAttributes.getValue("xml:base") != null) {
        String str = paramAttributes.getValue("xml:base");
        i = Catalog.BASE;
        vector.add(str);
        this.baseURIStack.push(str);
        Debug.message(4, "xml:base", str);
        try {
          CatalogEntry catalogEntry = new CatalogEntry(i, vector);
          this.catalog.addEntry(catalogEntry);
        } catch (CatalogException catalogException) {
          if (catalogException.getExceptionType() == 3) {
            Debug.message(1, "Invalid catalog entry type", paramString2);
          } else if (catalogException.getExceptionType() == 2) {
            Debug.message(1, "Invalid catalog entry (base)", paramString2);
          } 
        } 
        i = -1;
        vector = new Vector();
      } else {
        this.baseURIStack.push(this.baseURIStack.peek());
      } 
      if ((paramString2.equals("catalog") || paramString2.equals("group")) && paramAttributes.getValue("prefer") != null) {
        String str = paramAttributes.getValue("prefer");
        if (str.equals("public")) {
          str = "yes";
        } else if (str.equals("system")) {
          str = "no";
        } else {
          Debug.message(1, "Invalid prefer: must be 'system' or 'public'", paramString2);
          str = this.catalog.getDefaultOverride();
        } 
        i = Catalog.OVERRIDE;
        vector.add(str);
        this.overrideStack.push(str);
        Debug.message(4, "override", str);
        try {
          CatalogEntry catalogEntry = new CatalogEntry(i, vector);
          this.catalog.addEntry(catalogEntry);
        } catch (CatalogException catalogException) {
          if (catalogException.getExceptionType() == 3) {
            Debug.message(1, "Invalid catalog entry type", paramString2);
          } else if (catalogException.getExceptionType() == 2) {
            Debug.message(1, "Invalid catalog entry (override)", paramString2);
          } 
        } 
        i = -1;
        vector = new Vector();
      } else {
        this.overrideStack.push(this.overrideStack.peek());
      } 
      if (paramString2.equals("delegatePublic")) {
        if (checkAttributes(paramAttributes, "publicIdStartString", "catalog")) {
          i = Catalog.DELEGATE_PUBLIC;
          vector.add(paramAttributes.getValue("publicIdStartString"));
          vector.add(paramAttributes.getValue("catalog"));
          Debug.message(4, "delegatePublic", PublicId.normalize(paramAttributes.getValue("publicIdStartString")), paramAttributes.getValue("catalog"));
        } 
      } else if (paramString2.equals("delegateSystem")) {
        if (checkAttributes(paramAttributes, "systemIdStartString", "catalog")) {
          i = Catalog.DELEGATE_SYSTEM;
          vector.add(paramAttributes.getValue("systemIdStartString"));
          vector.add(paramAttributes.getValue("catalog"));
          Debug.message(4, "delegateSystem", paramAttributes.getValue("systemIdStartString"), paramAttributes.getValue("catalog"));
        } 
      } else if (paramString2.equals("delegateURI")) {
        if (checkAttributes(paramAttributes, "uriStartString", "catalog")) {
          i = Catalog.DELEGATE_URI;
          vector.add(paramAttributes.getValue("uriStartString"));
          vector.add(paramAttributes.getValue("catalog"));
          Debug.message(4, "delegateURI", paramAttributes.getValue("uriStartString"), paramAttributes.getValue("catalog"));
        } 
      } else if (paramString2.equals("rewriteSystem")) {
        if (checkAttributes(paramAttributes, "systemIdStartString", "rewritePrefix")) {
          i = Catalog.REWRITE_SYSTEM;
          vector.add(paramAttributes.getValue("systemIdStartString"));
          vector.add(paramAttributes.getValue("rewritePrefix"));
          Debug.message(4, "rewriteSystem", paramAttributes.getValue("systemIdStartString"), paramAttributes.getValue("rewritePrefix"));
        } 
      } else if (paramString2.equals("rewriteURI")) {
        if (checkAttributes(paramAttributes, "uriStartString", "rewritePrefix")) {
          i = Catalog.REWRITE_URI;
          vector.add(paramAttributes.getValue("uriStartString"));
          vector.add(paramAttributes.getValue("rewritePrefix"));
          Debug.message(4, "rewriteURI", paramAttributes.getValue("uriStartString"), paramAttributes.getValue("rewritePrefix"));
        } 
      } else if (paramString2.equals("nextCatalog")) {
        if (checkAttributes(paramAttributes, "catalog")) {
          i = Catalog.CATALOG;
          vector.add(paramAttributes.getValue("catalog"));
          Debug.message(4, "nextCatalog", paramAttributes.getValue("catalog"));
        } 
      } else if (paramString2.equals("public")) {
        if (checkAttributes(paramAttributes, "publicId", "uri")) {
          i = Catalog.PUBLIC;
          vector.add(paramAttributes.getValue("publicId"));
          vector.add(paramAttributes.getValue("uri"));
          Debug.message(4, "public", PublicId.normalize(paramAttributes.getValue("publicId")), paramAttributes.getValue("uri"));
        } 
      } else if (paramString2.equals("system")) {
        if (checkAttributes(paramAttributes, "systemId", "uri")) {
          i = Catalog.SYSTEM;
          vector.add(paramAttributes.getValue("systemId"));
          vector.add(paramAttributes.getValue("uri"));
          Debug.message(4, "system", paramAttributes.getValue("systemId"), paramAttributes.getValue("uri"));
        } 
      } else if (paramString2.equals("uri")) {
        if (checkAttributes(paramAttributes, "name", "uri")) {
          i = Catalog.URI;
          vector.add(paramAttributes.getValue("name"));
          vector.add(paramAttributes.getValue("uri"));
          Debug.message(4, "uri", paramAttributes.getValue("name"), paramAttributes.getValue("uri"));
        } 
      } else if (!paramString2.equals("catalog") && !paramString2.equals("group")) {
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
    if (paramString1 != null && "urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(paramString1) && !bool) {
      if (paramAttributes.getValue("xml:base") != null) {
        String str = paramAttributes.getValue("xml:base");
        i = Catalog.BASE;
        vector.add(str);
        this.baseURIStack.push(str);
        Debug.message(4, "xml:base", str);
        try {
          CatalogEntry catalogEntry = new CatalogEntry(i, vector);
          this.catalog.addEntry(catalogEntry);
        } catch (CatalogException catalogException) {
          if (catalogException.getExceptionType() == 3) {
            Debug.message(1, "Invalid catalog entry type", paramString2);
          } else if (catalogException.getExceptionType() == 2) {
            Debug.message(1, "Invalid catalog entry (base)", paramString2);
          } 
        } 
        i = -1;
        vector = new Vector();
      } else {
        this.baseURIStack.push(this.baseURIStack.peek());
      } 
      if (paramString2.equals("doctype")) {
        this;
        i = Catalog.DOCTYPE;
        vector.add(paramAttributes.getValue("name"));
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("document")) {
        this;
        i = Catalog.DOCUMENT;
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("dtddecl")) {
        this;
        i = Catalog.DTDDECL;
        vector.add(paramAttributes.getValue("publicId"));
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("entity")) {
        i = Catalog.ENTITY;
        vector.add(paramAttributes.getValue("name"));
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("linktype")) {
        i = Catalog.LINKTYPE;
        vector.add(paramAttributes.getValue("name"));
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("notation")) {
        i = Catalog.NOTATION;
        vector.add(paramAttributes.getValue("name"));
        vector.add(paramAttributes.getValue("uri"));
      } else if (paramString2.equals("sgmldecl")) {
        i = Catalog.SGMLDECL;
        vector.add(paramAttributes.getValue("uri"));
      } else {
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
  }
  
  public boolean checkAttributes(Attributes paramAttributes, String paramString) {
    if (paramAttributes.getValue(paramString) == null) {
      Debug.message(1, "Error: required attribute " + paramString + " missing.");
      return false;
    } 
    return true;
  }
  
  public boolean checkAttributes(Attributes paramAttributes, String paramString1, String paramString2) {
    return (checkAttributes(paramAttributes, paramString1) && checkAttributes(paramAttributes, paramString2));
  }
  
  public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {
    int i = -1;
    Vector vector = new Vector();
    boolean bool = inExtensionNamespace();
    if (paramString1 != null && !bool && ("urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(paramString1) || "urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(paramString1))) {
      String str1 = this.baseURIStack.pop();
      String str2 = this.baseURIStack.peek();
      if (!str2.equals(str1)) {
        this;
        i = Catalog.BASE;
        vector.add(str2);
        Debug.message(4, "(reset) xml:base", str2);
        try {
          CatalogEntry catalogEntry = new CatalogEntry(i, vector);
          this.catalog.addEntry(catalogEntry);
        } catch (CatalogException catalogException) {
          if (catalogException.getExceptionType() == 3) {
            Debug.message(1, "Invalid catalog entry type", paramString2);
          } else if (catalogException.getExceptionType() == 2) {
            Debug.message(1, "Invalid catalog entry (rbase)", paramString2);
          } 
        } 
      } 
    } 
    if (paramString1 != null && "urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(paramString1) && !bool && (paramString2.equals("catalog") || paramString2.equals("group"))) {
      String str1 = this.overrideStack.pop();
      String str2 = this.overrideStack.peek();
      if (!str2.equals(str1)) {
        this;
        i = Catalog.OVERRIDE;
        vector.add(str2);
        this.overrideStack.push(str2);
        Debug.message(4, "(reset) override", str2);
        try {
          CatalogEntry catalogEntry = new CatalogEntry(i, vector);
          this.catalog.addEntry(catalogEntry);
        } catch (CatalogException catalogException) {
          if (catalogException.getExceptionType() == 3) {
            Debug.message(1, "Invalid catalog entry type", paramString2);
          } else if (catalogException.getExceptionType() == 2) {
            Debug.message(1, "Invalid catalog entry (roverride)", paramString2);
          } 
        } 
      } 
    } 
    this.namespaceStack.pop();
  }
  
  public void characters(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {}
  
  public void ignorableWhitespace(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws SAXException {}
  
  public void processingInstruction(String paramString1, String paramString2) throws SAXException {}
  
  public void skippedEntity(String paramString) throws SAXException {}
  
  public void startPrefixMapping(String paramString1, String paramString2) throws SAXException {}
  
  public void endPrefixMapping(String paramString) throws SAXException {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\OASISXMLCatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */