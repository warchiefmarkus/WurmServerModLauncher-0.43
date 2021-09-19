package javax.jnlp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface PersistenceService {
  public static final int CACHED = 0;
  
  public static final int TEMPORARY = 1;
  
  public static final int DIRTY = 2;
  
  long create(URL paramURL, long paramLong) throws MalformedURLException, IOException;
  
  FileContents get(URL paramURL) throws MalformedURLException, IOException, FileNotFoundException;
  
  void delete(URL paramURL) throws MalformedURLException, IOException;
  
  String[] getNames(URL paramURL) throws MalformedURLException, IOException;
  
  int getTag(URL paramURL) throws MalformedURLException, IOException;
  
  void setTag(URL paramURL, int paramInt) throws MalformedURLException, IOException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\PersistenceService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */