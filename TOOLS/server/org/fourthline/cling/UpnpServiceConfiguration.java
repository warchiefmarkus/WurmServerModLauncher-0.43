package org.fourthline.cling;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import org.fourthline.cling.binding.xml.DeviceDescriptorBinder;
import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
import org.fourthline.cling.model.Namespace;
import org.fourthline.cling.model.message.UpnpHeaders;
import org.fourthline.cling.model.meta.RemoteDeviceIdentity;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.transport.spi.DatagramIO;
import org.fourthline.cling.transport.spi.DatagramProcessor;
import org.fourthline.cling.transport.spi.GENAEventProcessor;
import org.fourthline.cling.transport.spi.MulticastReceiver;
import org.fourthline.cling.transport.spi.NetworkAddressFactory;
import org.fourthline.cling.transport.spi.SOAPActionProcessor;
import org.fourthline.cling.transport.spi.StreamClient;
import org.fourthline.cling.transport.spi.StreamServer;

public interface UpnpServiceConfiguration {
  NetworkAddressFactory createNetworkAddressFactory();
  
  DatagramProcessor getDatagramProcessor();
  
  SOAPActionProcessor getSoapActionProcessor();
  
  GENAEventProcessor getGenaEventProcessor();
  
  StreamClient createStreamClient();
  
  MulticastReceiver createMulticastReceiver(NetworkAddressFactory paramNetworkAddressFactory);
  
  DatagramIO createDatagramIO(NetworkAddressFactory paramNetworkAddressFactory);
  
  StreamServer createStreamServer(NetworkAddressFactory paramNetworkAddressFactory);
  
  Executor getMulticastReceiverExecutor();
  
  Executor getDatagramIOExecutor();
  
  ExecutorService getStreamServerExecutorService();
  
  DeviceDescriptorBinder getDeviceDescriptorBinderUDA10();
  
  ServiceDescriptorBinder getServiceDescriptorBinderUDA10();
  
  ServiceType[] getExclusiveServiceTypes();
  
  int getRegistryMaintenanceIntervalMillis();
  
  int getAliveIntervalMillis();
  
  boolean isReceivedSubscriptionTimeoutIgnored();
  
  Integer getRemoteDeviceMaxAgeSeconds();
  
  UpnpHeaders getDescriptorRetrievalHeaders(RemoteDeviceIdentity paramRemoteDeviceIdentity);
  
  UpnpHeaders getEventSubscriptionHeaders(RemoteService paramRemoteService);
  
  Executor getAsyncProtocolExecutor();
  
  ExecutorService getSyncProtocolExecutorService();
  
  Namespace getNamespace();
  
  Executor getRegistryMaintainerExecutor();
  
  Executor getRegistryListenerExecutor();
  
  void shutdown();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\UpnpServiceConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */