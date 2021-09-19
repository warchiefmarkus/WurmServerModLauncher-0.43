package javax.jnlp;

import java.io.IOException;
import java.net.URL;

public interface DownloadService {
  boolean isResourceCached(URL paramURL, String paramString);
  
  boolean isPartCached(String paramString);
  
  boolean isPartCached(String[] paramArrayOfString);
  
  boolean isExtensionPartCached(URL paramURL, String paramString1, String paramString2);
  
  boolean isExtensionPartCached(URL paramURL, String paramString, String[] paramArrayOfString);
  
  void loadResource(URL paramURL, String paramString, DownloadServiceListener paramDownloadServiceListener) throws IOException;
  
  void loadPart(String paramString, DownloadServiceListener paramDownloadServiceListener) throws IOException;
  
  void loadPart(String[] paramArrayOfString, DownloadServiceListener paramDownloadServiceListener) throws IOException;
  
  void loadExtensionPart(URL paramURL, String paramString1, String paramString2, DownloadServiceListener paramDownloadServiceListener) throws IOException;
  
  void loadExtensionPart(URL paramURL, String paramString, String[] paramArrayOfString, DownloadServiceListener paramDownloadServiceListener) throws IOException;
  
  void removeResource(URL paramURL, String paramString) throws IOException;
  
  void removePart(String paramString) throws IOException;
  
  void removePart(String[] paramArrayOfString) throws IOException;
  
  void removeExtensionPart(URL paramURL, String paramString1, String paramString2) throws IOException;
  
  void removeExtensionPart(URL paramURL, String paramString, String[] paramArrayOfString) throws IOException;
  
  DownloadServiceListener getDefaultProgressWindow();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\DownloadService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */