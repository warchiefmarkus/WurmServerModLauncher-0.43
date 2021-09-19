package javax.jnlp;

import java.net.URL;

public interface ExtensionInstallerService {
  String getInstallPath();
  
  String getExtensionVersion();
  
  URL getExtensionLocation();
  
  void hideProgressBar();
  
  void hideStatusWindow();
  
  void setHeading(String paramString);
  
  void setStatus(String paramString);
  
  void updateProgress(int paramInt);
  
  void installSucceeded(boolean paramBoolean);
  
  void installFailed();
  
  void setJREInfo(String paramString1, String paramString2);
  
  void setNativeLibraryInfo(String paramString);
  
  String getInstalledJRE(URL paramURL, String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\jnlp\ExtensionInstallerService.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */