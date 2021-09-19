package org.fourthline.cling.registry;

import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;

public interface RegistryListener {
  void remoteDeviceDiscoveryStarted(Registry paramRegistry, RemoteDevice paramRemoteDevice);
  
  void remoteDeviceDiscoveryFailed(Registry paramRegistry, RemoteDevice paramRemoteDevice, Exception paramException);
  
  void remoteDeviceAdded(Registry paramRegistry, RemoteDevice paramRemoteDevice);
  
  void remoteDeviceUpdated(Registry paramRegistry, RemoteDevice paramRemoteDevice);
  
  void remoteDeviceRemoved(Registry paramRegistry, RemoteDevice paramRemoteDevice);
  
  void localDeviceAdded(Registry paramRegistry, LocalDevice paramLocalDevice);
  
  void localDeviceRemoved(Registry paramRegistry, LocalDevice paramLocalDevice);
  
  void beforeShutdown(Registry paramRegistry);
  
  void afterShutdown();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\RegistryListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */