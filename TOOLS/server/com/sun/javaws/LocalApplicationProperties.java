package com.sun.javaws;

import com.sun.javaws.jnl.AssociationDesc;
import com.sun.javaws.jnl.LaunchDesc;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public interface LocalApplicationProperties {
  URL getLocation();
  
  String getVersionId();
  
  LaunchDesc getLaunchDescriptor();
  
  void setLastAccessed(Date paramDate);
  
  Date getLastAccessed();
  
  int getLaunchCount();
  
  void incrementLaunchCount();
  
  void setAskedForInstall(boolean paramBoolean);
  
  boolean getAskedForInstall();
  
  void setRebootNeeded(boolean paramBoolean);
  
  boolean isRebootNeeded();
  
  void setLocallyInstalled(boolean paramBoolean);
  
  boolean isLocallyInstalled();
  
  boolean isLocallyInstalledSystem();
  
  boolean forceUpdateCheck();
  
  void setForceUpdateCheck(boolean paramBoolean);
  
  boolean isApplicationDescriptor();
  
  boolean isExtensionDescriptor();
  
  AssociationDesc[] getAssociations();
  
  void addAssociation(AssociationDesc paramAssociationDesc);
  
  void setAssociations(AssociationDesc[] paramArrayOfAssociationDesc);
  
  String getNativeLibDirectory();
  
  String getInstallDirectory();
  
  void setNativeLibDirectory(String paramString);
  
  void setInstallDirectory(String paramString);
  
  String getRegisteredTitle();
  
  void setRegisteredTitle(String paramString);
  
  void put(String paramString1, String paramString2);
  
  String get(String paramString);
  
  int getInteger(String paramString);
  
  boolean getBoolean(String paramString);
  
  Date getDate(String paramString);
  
  void store() throws IOException;
  
  void refreshIfNecessary();
  
  void refresh();
  
  boolean isShortcutSupported();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\LocalApplicationProperties.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */