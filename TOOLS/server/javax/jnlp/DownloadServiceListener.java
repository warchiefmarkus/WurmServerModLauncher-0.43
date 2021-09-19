package javax.jnlp;

import java.net.URL;

public interface DownloadServiceListener {
  void progress(URL paramURL, String paramString, long paramLong1, long paramLong2, int paramInt);
  
  void validating(URL paramURL, String paramString, long paramLong1, long paramLong2, int paramInt);
  
  void upgradingArchive(URL paramURL, String paramString, int paramInt1, int paramInt2);
  
  void downloadFailed(URL paramURL, String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\DownloadServiceListener.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */