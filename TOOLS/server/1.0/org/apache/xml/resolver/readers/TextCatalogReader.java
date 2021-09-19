package 1.0.org.apache.xml.resolver.readers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Stack;
import java.util.Vector;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogEntry;
import org.apache.xml.resolver.CatalogException;
import org.apache.xml.resolver.helpers.Debug;
import org.apache.xml.resolver.readers.CatalogReader;

public class TextCatalogReader implements CatalogReader {
  protected InputStream catfile = null;
  
  protected int[] stack = new int[3];
  
  protected Stack tokenStack = new Stack();
  
  protected int top = -1;
  
  protected boolean caseSensitive = false;
  
  public void setCaseSensitive(boolean paramBoolean) {
    this.caseSensitive = paramBoolean;
  }
  
  public boolean getCaseSensitive() {
    return this.caseSensitive;
  }
  
  public void readCatalog(Catalog paramCatalog, String paramString) throws MalformedURLException, IOException {
    URL uRL = null;
    try {
      uRL = new URL(paramString);
    } catch (MalformedURLException malformedURLException) {
      uRL = new URL("file:///" + paramString);
    } 
    URLConnection uRLConnection = uRL.openConnection();
    try {
      readCatalog(paramCatalog, uRLConnection.getInputStream());
    } catch (FileNotFoundException fileNotFoundException) {
      Debug.message(1, "Failed to load catalog, file not found", uRL.toString());
    } 
  }
  
  public void readCatalog(Catalog paramCatalog, InputStream paramInputStream) throws MalformedURLException, IOException {
    this.catfile = paramInputStream;
    if (this.catfile == null)
      return; 
    Vector vector = null;
    while (true) {
      String str1 = nextToken();
      if (str1 == null) {
        if (vector != null) {
          paramCatalog.unknownEntry(vector);
          vector = null;
        } 
        this.catfile.close();
        this.catfile = null;
        return;
      } 
      String str2 = null;
      if (this.caseSensitive) {
        str2 = str1;
      } else {
        str2 = str1.toUpperCase();
      } 
      try {
        int i = CatalogEntry.getEntryType(str2);
        int j = CatalogEntry.getEntryArgCount(i);
        Vector vector1 = new Vector();
        if (vector != null) {
          paramCatalog.unknownEntry(vector);
          vector = null;
        } 
        for (byte b = 0; b < j; b++)
          vector1.addElement(nextToken()); 
        paramCatalog.addEntry(new CatalogEntry(str2, vector1));
      } catch (CatalogException catalogException) {
        if (catalogException.getExceptionType() == 3) {
          if (vector == null)
            vector = new Vector(); 
          vector.addElement(str1);
          continue;
        } 
        if (catalogException.getExceptionType() == 2) {
          Debug.message(1, "Invalid catalog entry", str1);
          vector = null;
        } 
      } 
    } 
  }
  
  protected void finalize() {
    if (this.catfile != null)
      try {
        this.catfile.close();
      } catch (IOException iOException) {} 
    this.catfile = null;
  }
  
  protected String nextToken() throws IOException {
    int j;
    String str = "";
    if (!this.tokenStack.empty())
      return this.tokenStack.pop(); 
    label44: while (true) {
      i = this.catfile.read();
      while (i <= 32) {
        i = this.catfile.read();
        if (i < 0)
          return null; 
      } 
      j = this.catfile.read();
      if (j < 0)
        return null; 
      if (i == 45 && j == 45) {
        i = 32;
        for (j = nextChar();; j = nextChar()) {
          if (i == 45 && j == 45)
            continue label44; 
          i = j;
        } 
      } 
      break;
    } 
    this.stack[++this.top] = j;
    this.stack[++this.top] = i;
    int i = nextChar();
    if (i == 34 || i == 39) {
      int k = i;
      while ((i = nextChar()) != k) {
        char[] arrayOfChar = new char[1];
        arrayOfChar[0] = (char)i;
        String str1 = new String(arrayOfChar);
        str = str.concat(str1);
      } 
      return str;
    } 
    while (i > 32) {
      j = nextChar();
      if (i == 45 && j == 45) {
        this.stack[++this.top] = i;
        this.stack[++this.top] = j;
        return str;
      } 
      char[] arrayOfChar = new char[1];
      arrayOfChar[0] = (char)i;
      String str1 = new String(arrayOfChar);
      str = str.concat(str1);
      i = j;
    } 
    return str;
  }
  
  protected int nextChar() throws IOException {
    return (this.top < 0) ? this.catfile.read() : this.stack[this.top--];
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\TextCatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */