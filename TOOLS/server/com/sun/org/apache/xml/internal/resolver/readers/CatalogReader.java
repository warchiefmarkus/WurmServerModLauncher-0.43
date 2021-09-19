package com.sun.org.apache.xml.internal.resolver.readers;

import com.sun.org.apache.xml.internal.resolver.Catalog;
import com.sun.org.apache.xml.internal.resolver.CatalogException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public interface CatalogReader {
  void readCatalog(Catalog paramCatalog, String paramString) throws MalformedURLException, IOException, CatalogException;
  
  void readCatalog(Catalog paramCatalog, InputStream paramInputStream) throws IOException, CatalogException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\readers\CatalogReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */