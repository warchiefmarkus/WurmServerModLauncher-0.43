package 1.0.org.apache.xml.resolver.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import org.apache.xml.resolver.Catalog;
import org.apache.xml.resolver.CatalogException;

public interface CatalogReader {
  void readCatalog(Catalog paramCatalog, String paramString) throws MalformedURLException, IOException, CatalogException;
  
  void readCatalog(Catalog paramCatalog, InputStream paramInputStream) throws IOException, CatalogException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\readers\CatalogReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */