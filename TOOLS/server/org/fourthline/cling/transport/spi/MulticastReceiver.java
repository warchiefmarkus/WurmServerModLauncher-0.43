package org.fourthline.cling.transport.spi;

import java.net.NetworkInterface;
import org.fourthline.cling.transport.Router;

public interface MulticastReceiver<C extends MulticastReceiverConfiguration> extends Runnable {
  void init(NetworkInterface paramNetworkInterface, Router paramRouter, NetworkAddressFactory paramNetworkAddressFactory, DatagramProcessor paramDatagramProcessor) throws InitializationException;
  
  void stop();
  
  C getConfiguration();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\MulticastReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */