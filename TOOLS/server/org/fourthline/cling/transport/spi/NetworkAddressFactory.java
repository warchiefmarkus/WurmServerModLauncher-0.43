package org.fourthline.cling.transport.spi;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Iterator;

public interface NetworkAddressFactory {
  public static final String SYSTEM_PROPERTY_NET_IFACES = "org.fourthline.cling.network.useInterfaces";
  
  public static final String SYSTEM_PROPERTY_NET_ADDRESSES = "org.fourthline.cling.network.useAddresses";
  
  InetAddress getMulticastGroup();
  
  int getMulticastPort();
  
  int getStreamListenPort();
  
  Iterator<NetworkInterface> getNetworkInterfaces();
  
  Iterator<InetAddress> getBindAddresses();
  
  boolean hasUsableNetwork();
  
  Short getAddressNetworkPrefixLength(InetAddress paramInetAddress);
  
  byte[] getHardwareAddress(InetAddress paramInetAddress);
  
  InetAddress getBroadcastAddress(InetAddress paramInetAddress);
  
  InetAddress getLocalAddress(NetworkInterface paramNetworkInterface, boolean paramBoolean, InetAddress paramInetAddress) throws IllegalStateException;
  
  void logInterfaceInformation();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\NetworkAddressFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */