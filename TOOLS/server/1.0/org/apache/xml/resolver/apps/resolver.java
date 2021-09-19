package 1.0.org.apache.xml.resolver.apps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.tools.CatalogResolver;

public class resolver {
  public static void main(String[] paramArrayOfString) throws FileNotFoundException, IOException {
    int i = 0;
    Vector vector = new Vector();
    int j = 0;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    boolean bool = false;
    for (byte b1 = 0; b1 < paramArrayOfString.length; b1++) {
      if (paramArrayOfString[b1].equals("-c")) {
        vector.add(paramArrayOfString[++b1]);
      } else if (paramArrayOfString[b1].equals("-p")) {
        str3 = paramArrayOfString[++b1];
      } else if (paramArrayOfString[b1].equals("-s")) {
        str4 = paramArrayOfString[++b1];
      } else if (paramArrayOfString[b1].equals("-n")) {
        str2 = paramArrayOfString[++b1];
      } else if (paramArrayOfString[b1].equals("-u")) {
        str5 = paramArrayOfString[++b1];
      } else if (paramArrayOfString[b1].equals("-a")) {
        bool = true;
      } else if (paramArrayOfString[b1].equals("-d")) {
        String str = paramArrayOfString[++b1];
        try {
          i = Integer.parseInt(str);
          if (i >= 0)
            Debug.setDebug(i); 
        } catch (Exception exception) {}
      } else {
        str1 = paramArrayOfString[b1];
      } 
    } 
    if (str1 == null)
      usage(); 
    if (str1.equalsIgnoreCase("doctype")) {
      j = Catalog.DOCTYPE;
      if (str3 == null && str4 == null) {
        System.out.println("DOCTYPE requires public or system identifier.");
        usage();
      } 
    } else if (str1.equalsIgnoreCase("document")) {
      j = Catalog.DOCUMENT;
    } else if (str1.equalsIgnoreCase("entity")) {
      j = Catalog.ENTITY;
      if (str3 == null && str4 == null && str2 == null) {
        System.out.println("ENTITY requires name or public or system identifier.");
        usage();
      } 
    } else if (str1.equalsIgnoreCase("notation")) {
      j = Catalog.NOTATION;
      if (str3 == null && str4 == null && str2 == null) {
        System.out.println("NOTATION requires name or public or system identifier.");
        usage();
      } 
    } else if (str1.equalsIgnoreCase("public")) {
      j = Catalog.PUBLIC;
      if (str3 == null) {
        System.out.println("PUBLIC requires public identifier.");
        usage();
      } 
    } else if (str1.equalsIgnoreCase("system")) {
      j = Catalog.SYSTEM;
      if (str4 == null) {
        System.out.println("SYSTEM requires system identifier.");
        usage();
      } 
    } else if (str1.equalsIgnoreCase("uri")) {
      j = Catalog.URI;
      if (str5 == null) {
        System.out.println("URI requires a uri.");
        usage();
      } 
    } else {
      System.out.println(str1 + " is not a recognized keyword.");
      usage();
    } 
    if (bool) {
      URL uRL1 = null;
      URL uRL2 = null;
      try {
        String str = System.getProperty("user.dir");
        str.replace('\\', '/');
        uRL1 = new URL("file:///" + str + "/basename");
      } catch (MalformedURLException malformedURLException) {
        String str = System.getProperty("user.dir");
        str.replace('\\', '/');
        Debug.message(1, "Malformed URL on cwd", str);
        uRL1 = null;
      } 
      try {
        uRL2 = new URL(uRL1, str4);
        str4 = uRL2.toString();
      } catch (MalformedURLException malformedURLException) {
        try {
          uRL2 = new URL("file:///" + str4);
        } catch (MalformedURLException malformedURLException1) {
          Debug.message(1, "Malformed URL on system id", str4);
        } 
      } 
    } 
    CatalogResolver catalogResolver = new CatalogResolver();
    Catalog catalog = catalogResolver.getCatalog();
    for (byte b2 = 0; b2 < vector.size(); b2++) {
      String str = vector.elementAt(b2);
      catalog.parseCatalog(str);
    } 
    String str6 = null;
    if (j == Catalog.DOCTYPE) {
      System.out.println("Resolve DOCTYPE (name, publicid, systemid):");
      if (str2 != null)
        System.out.println("       name: " + str2); 
      if (str3 != null)
        System.out.println("  public id: " + str3); 
      if (str4 != null)
        System.out.println("  system id: " + str4); 
      if (str5 != null)
        System.out.println("        uri: " + str5); 
      str6 = catalog.resolveDoctype(str2, str3, str4);
    } else if (j == Catalog.DOCUMENT) {
      System.out.println("Resolve DOCUMENT ():");
      str6 = catalog.resolveDocument();
    } else if (j == Catalog.ENTITY) {
      System.out.println("Resolve ENTITY (name, publicid, systemid):");
      if (str2 != null)
        System.out.println("       name: " + str2); 
      if (str3 != null)
        System.out.println("  public id: " + str3); 
      if (str4 != null)
        System.out.println("  system id: " + str4); 
      str6 = catalog.resolveEntity(str2, str3, str4);
    } else if (j == Catalog.NOTATION) {
      System.out.println("Resolve NOTATION (name, publicid, systemid):");
      if (str2 != null)
        System.out.println("       name: " + str2); 
      if (str3 != null)
        System.out.println("  public id: " + str3); 
      if (str4 != null)
        System.out.println("  system id: " + str4); 
      str6 = catalog.resolveNotation(str2, str3, str4);
    } else if (j == Catalog.PUBLIC) {
      System.out.println("Resolve PUBLIC (publicid, systemid):");
      if (str3 != null)
        System.out.println("  public id: " + str3); 
      if (str4 != null)
        System.out.println("  system id: " + str4); 
      str6 = catalog.resolvePublic(str3, str4);
    } else if (j == Catalog.SYSTEM) {
      System.out.println("Resolve SYSTEM (systemid):");
      if (str4 != null)
        System.out.println("  system id: " + str4); 
      str6 = catalog.resolveSystem(str4);
    } else if (j == Catalog.URI) {
      System.out.println("Resolve URI (uri):");
      if (str5 != null)
        System.out.println("        uri: " + str5); 
      str6 = catalog.resolveURI(str5);
    } else {
      System.out.println("resType is wrong!? This can't happen!");
      usage();
    } 
    System.out.println("Result: " + str6);
  }
  
  public static void usage() {
    System.out.println("Usage: resolver [options] keyword");
    System.out.println("");
    System.out.println("Where:");
    System.out.println("");
    System.out.println("-c catalogfile  Loads a particular catalog file.");
    System.out.println("-n name         Sets the name.");
    System.out.println("-p publicId     Sets the public identifier.");
    System.out.println("-s systemId     Sets the system identifier.");
    System.out.println("-a              Makes the system URI absolute before resolution");
    System.out.println("-u uri          Sets the URI.");
    System.out.println("-d integer      Set the debug level.");
    System.out.println("keyword         Identifies the type of resolution to perform:");
    System.out.println("                doctype, document, entity, notation, public, system,");
    System.out.println("                or uri.");
    System.exit(1);
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\apps\resolver.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */