package javax.jnlp;

import java.net.URL;

public interface BasicService {
  URL getCodeBase();
  
  boolean isOffline();
  
  boolean showDocument(URL paramURL);
  
  boolean isWebBrowserSupported();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\BasicService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */