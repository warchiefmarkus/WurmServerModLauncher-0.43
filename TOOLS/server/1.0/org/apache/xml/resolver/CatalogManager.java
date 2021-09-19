package 1.0.org.apache.xml.resolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

public class CatalogManager {
  private static String pFiles = "xml.catalog.files";
  
  private static String pVerbosity = "xml.catalog.verbosity";
  
  private static String pPrefer = "xml.catalog.prefer";
  
  private static String pStatic = "xml.catalog.staticCatalog";
  
  private static String pAllowPI = "xml.catalog.allowPI";
  
  private static String pClassname = "xml.catalog.className";
  
  private static String pIgnoreMissing = "xml.catalog.ignoreMissing";
  
  private static boolean ignoreMissingProperties = (System.getProperty(pIgnoreMissing) != null || System.getProperty(pFiles) != null);
  
  private static ResourceBundle resources;
  
  private static String propertyFile = "CatalogManager.properties";
  
  private static URL propertyFileURI = null;
  
  private static String defaultCatalogFiles = "./xcatalog";
  
  private static int defaultVerbosity = 1;
  
  private static boolean defaultPreferPublic = true;
  
  private static boolean defaultStaticCatalog = true;
  
  private static boolean defaultOasisXMLCatalogPI = true;
  
  private static boolean defaultRelativeCatalogs = true;
  
  private static synchronized void readProperties() {
    try {
      propertyFileURI = org.apache.xml.resolver.CatalogManager.class.getResource("/" + propertyFile);
      InputStream inputStream = org.apache.xml.resolver.CatalogManager.class.getResourceAsStream("/" + propertyFile);
      if (inputStream == null) {
        if (!ignoreMissingProperties)
          System.err.println("Cannot find " + propertyFile); 
        return;
      } 
      resources = new PropertyResourceBundle(inputStream);
    } catch (MissingResourceException missingResourceException) {
      if (!ignoreMissingProperties)
        System.err.println("Cannot read " + propertyFile); 
    } catch (IOException iOException) {
      if (!ignoreMissingProperties)
        System.err.println("Failure trying to read " + propertyFile); 
    } 
  }
  
  public static void ignoreMissingProperties(boolean paramBoolean) {
    ignoreMissingProperties = paramBoolean;
  }
  
  public static int verbosity() {
    String str = System.getProperty(pVerbosity);
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources == null)
        return defaultVerbosity; 
      try {
        str = resources.getString("verbosity");
      } catch (MissingResourceException missingResourceException) {
        return defaultVerbosity;
      } 
    } 
    try {
      return Integer.parseInt(str.trim());
    } catch (Exception exception) {
      System.err.println("Cannot parse verbosity: \"" + str + "\"");
      return defaultVerbosity;
    } 
  }
  
  public static boolean relativeCatalogs() {
    if (resources == null)
      readProperties(); 
    if (resources == null)
      return defaultRelativeCatalogs; 
    try {
      String str = resources.getString("relative-catalogs");
      return (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("1"));
    } catch (MissingResourceException missingResourceException) {
      return defaultRelativeCatalogs;
    } 
  }
  
  public static Vector catalogFiles() {
    String str = System.getProperty(pFiles);
    boolean bool = false;
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources != null)
        try {
          str = resources.getString("catalogs");
          bool = true;
        } catch (MissingResourceException missingResourceException) {
          System.err.println(propertyFile + ": catalogs not found.");
          str = null;
        }  
    } 
    if (str == null)
      str = defaultCatalogFiles; 
    StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
    Vector vector = new Vector();
    while (stringTokenizer.hasMoreTokens()) {
      String str1 = stringTokenizer.nextToken();
      URL uRL = null;
      if (bool && !relativeCatalogs())
        try {
          uRL = new URL(propertyFileURI, str1);
          str1 = uRL.toString();
        } catch (MalformedURLException malformedURLException) {
          uRL = null;
        }  
      vector.add(str1);
    } 
    return vector;
  }
  
  public static boolean preferPublic() {
    String str = System.getProperty(pPrefer);
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources == null)
        return defaultPreferPublic; 
      try {
        str = resources.getString("prefer");
      } catch (MissingResourceException missingResourceException) {
        return defaultPreferPublic;
      } 
    } 
    return (str == null) ? defaultPreferPublic : str.equalsIgnoreCase("public");
  }
  
  public static boolean staticCatalog() {
    String str = System.getProperty(pStatic);
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources == null)
        return defaultStaticCatalog; 
      try {
        str = resources.getString("static-catalog");
      } catch (MissingResourceException missingResourceException) {
        return defaultStaticCatalog;
      } 
    } 
    return (str == null) ? defaultStaticCatalog : ((str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("1")));
  }
  
  public static boolean allowOasisXMLCatalogPI() {
    String str = System.getProperty(pAllowPI);
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources == null)
        return defaultOasisXMLCatalogPI; 
      try {
        str = resources.getString("allow-oasis-xml-catalog-pi");
      } catch (MissingResourceException missingResourceException) {
        return defaultOasisXMLCatalogPI;
      } 
    } 
    return (str == null) ? defaultOasisXMLCatalogPI : ((str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("1")));
  }
  
  public static String catalogClassName() {
    String str = System.getProperty(pClassname);
    if (str == null) {
      if (resources == null)
        readProperties(); 
      if (resources == null)
        return null; 
      try {
        return resources.getString("catalog-class-name");
      } catch (MissingResourceException missingResourceException) {
        return null;
      } 
    } 
    return str;
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\CatalogManager.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */