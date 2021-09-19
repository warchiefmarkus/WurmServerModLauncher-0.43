package 1.0.org.apache.xml.resolver.apps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.apps.XParseError;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class xread {
  public static void main(String[] paramArrayOfString) throws FileNotFoundException, IOException {
    String str1 = null;
    int i = 0;
    int j = 10;
    boolean bool1 = true;
    boolean bool2 = true;
    boolean bool3 = (i > 2) ? true : false;
    boolean bool4 = true;
    Vector vector = new Vector();
    for (byte b1 = 0; b1 < paramArrayOfString.length; b1++) {
      if (paramArrayOfString[b1].equals("-c")) {
        vector.add(paramArrayOfString[++b1]);
      } else if (paramArrayOfString[b1].equals("-w")) {
        bool2 = false;
      } else if (paramArrayOfString[b1].equals("-v")) {
        bool2 = true;
      } else if (paramArrayOfString[b1].equals("-n")) {
        bool1 = false;
      } else if (paramArrayOfString[b1].equals("-N")) {
        bool1 = true;
      } else if (paramArrayOfString[b1].equals("-d")) {
        String str = paramArrayOfString[++b1];
        try {
          i = Integer.parseInt(str);
          if (i >= 0) {
            Debug.setDebug(i);
            bool3 = (i > 2) ? true : false;
          } 
        } catch (Exception exception) {}
      } else if (paramArrayOfString[b1].equals("-E")) {
        String str = paramArrayOfString[++b1];
        try {
          int k = Integer.parseInt(str);
          if (k >= 0)
            j = k; 
        } catch (Exception exception) {}
      } else {
        str1 = paramArrayOfString[b1];
      } 
    } 
    if (str1 == null) {
      System.out.println("Usage: org.apache.xml.resolver.apps.xread [opts] xmlfile");
      System.exit(1);
    } 
    ResolvingXMLReader resolvingXMLReader = new ResolvingXMLReader();
    try {
      resolvingXMLReader.setFeature("http://xml.org/sax/features/namespaces", bool1);
      resolvingXMLReader.setFeature("http://xml.org/sax/features/validation", bool2);
    } catch (SAXException sAXException) {}
    Catalog catalog = resolvingXMLReader.getCatalog();
    for (byte b2 = 0; b2 < vector.size(); b2++) {
      String str = vector.elementAt(b2);
      catalog.parseCatalog(str);
    } 
    XParseError xParseError = new XParseError(bool4, bool3);
    xParseError.setMaxMessages(j);
    resolvingXMLReader.setErrorHandler((ErrorHandler)xParseError);
    String str2 = bool2 ? "validating" : "well-formed";
    String str3 = bool1 ? "namespace-aware" : "namespace-ignorant";
    if (j > 0)
      System.out.println("Attempting " + str2 + ", " + str3 + " parse"); 
    Date date1 = new Date();
    try {
      resolvingXMLReader.parse(str1);
    } catch (SAXException sAXException) {
      System.out.println("SAX Exception: " + sAXException);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    Date date2 = new Date();
    long l1 = date2.getTime() - date1.getTime();
    long l2 = 0L;
    long l3 = 0L;
    long l4 = 0L;
    if (l1 > 1000L) {
      l2 = l1 / 1000L;
      l1 %= 1000L;
    } 
    if (l2 > 60L) {
      l3 = l2 / 60L;
      l2 %= 60L;
    } 
    if (l3 > 60L) {
      l4 = l3 / 60L;
      l3 %= 60L;
    } 
    if (j > 0) {
      System.out.print("Parse ");
      if (xParseError.getFatalCount() > 0) {
        System.out.print("failed ");
      } else {
        System.out.print("succeeded ");
        System.out.print("(");
        if (l4 > 0L)
          System.out.print(l4 + ":"); 
        if (l4 > 0L || l3 > 0L)
          System.out.print(l3 + ":"); 
        System.out.print(l2 + "." + l1);
        System.out.print(") ");
      } 
      System.out.print("with ");
      int k = xParseError.getErrorCount();
      int m = xParseError.getWarningCount();
      if (k > 0) {
        System.out.print(k + " error");
        System.out.print((k > 1) ? "s" : "");
        System.out.print(" and ");
      } else {
        System.out.print("no errors and ");
      } 
      if (m > 0) {
        System.out.print(m + " warning");
        System.out.print((m > 1) ? "s" : "");
        System.out.print(".");
      } else {
        System.out.print("no warnings.");
      } 
      System.out.println("");
    } 
    if (xParseError.getErrorCount() > 0)
      System.exit(1); 
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\apps\xread.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */