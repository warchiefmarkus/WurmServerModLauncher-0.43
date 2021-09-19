package org.fourthline.cling.registry;

import java.net.URI;
import java.util.Collection;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.model.DiscoveryOptions;
import org.fourthline.cling.model.ServiceReference;
import org.fourthline.cling.model.gena.LocalGENASubscription;
import org.fourthline.cling.model.gena.RemoteGENASubscription;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.resource.Resource;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.protocol.ProtocolFactory;

public interface Registry {
  UpnpService getUpnpService();
  
  UpnpServiceConfiguration getConfiguration();
  
  ProtocolFactory getProtocolFactory();
  
  void shutdown();
  
  void pause();
  
  void resume();
  
  boolean isPaused();
  
  void addListener(RegistryListener paramRegistryListener);
  
  void removeListener(RegistryListener paramRegistryListener);
  
  Collection<RegistryListener> getListeners();
  
  boolean notifyDiscoveryStart(RemoteDevice paramRemoteDevice);
  
  void notifyDiscoveryFailure(RemoteDevice paramRemoteDevice, Exception paramException);
  
  void addDevice(LocalDevice paramLocalDevice) throws RegistrationException;
  
  void addDevice(LocalDevice paramLocalDevice, DiscoveryOptions paramDiscoveryOptions) throws RegistrationException;
  
  void setDiscoveryOptions(UDN paramUDN, DiscoveryOptions paramDiscoveryOptions);
  
  DiscoveryOptions getDiscoveryOptions(UDN paramUDN);
  
  void addDevice(RemoteDevice paramRemoteDevice) throws RegistrationException;
  
  boolean update(RemoteDeviceIdentity paramRemoteDeviceIdentity);
  
  boolean removeDevice(LocalDevice paramLocalDevice);
  
  boolean removeDevice(RemoteDevice paramRemoteDevice);
  
  boolean removeDevice(UDN paramUDN);
  
  void removeAllLocalDevices();
  
  void removeAllRemoteDevices();
  
  Device getDevice(UDN paramUDN, boolean paramBoolean);
  
  LocalDevice getLocalDevice(UDN paramUDN, boolean paramBoolean);
  
  RemoteDevice getRemoteDevice(UDN paramUDN, boolean paramBoolean);
  
  Collection<LocalDevice> getLocalDevices();
  
  Collection<RemoteDevice> getRemoteDevices();
  
  Collection<Device> getDevices();
  
  Collection<Device> getDevices(DeviceType paramDeviceType);
  
  Collection<Device> getDevices(ServiceType paramServiceType);
  
  Service getService(ServiceReference paramServiceReference);
  
  void addResource(Resource paramResource);
  
  void addResource(Resource paramResource, int paramInt);
  
  boolean removeResource(Resource paramResource);
  
  Resource getResource(URI paramURI) throws IllegalArgumentException;
  
  <T extends Resource> T getResource(Class<T> paramClass, URI paramURI) throws IllegalArgumentException;
  
  Collection<Resource> getResources();
  
  <T extends Resource> Collection<T> getResources(Class<T> paramClass);
  
  void addLocalSubscription(LocalGENASubscription paramLocalGENASubscription);
  
  LocalGENASubscription getLocalSubscription(String paramString);
  
  boolean updateLocalSubscription(LocalGENASubscription paramLocalGENASubscription);
  
  boolean removeLocalSubscription(LocalGENASubscription paramLocalGENASubscription);
  
  void addRemoteSubscription(RemoteGENASubscription paramRemoteGENASubscription);
  
  RemoteGENASubscription getRemoteSubscription(String paramString);
  
  void updateRemoteSubscription(RemoteGENASubscription paramRemoteGENASubscription);
  
  void removeRemoteSubscription(RemoteGENASubscription paramRemoteGENASubscription);
  
  void registerPendingRemoteSubscription(RemoteGENASubscription paramRemoteGENASubscription);
  
  void unregisterPendingRemoteSubscription(RemoteGENASubscription paramRemoteGENASubscription);
  
  RemoteGENASubscription getWaitRemoteSubscription(String paramString);
  
  void advertiseLocalDevices();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\registry\Registry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */