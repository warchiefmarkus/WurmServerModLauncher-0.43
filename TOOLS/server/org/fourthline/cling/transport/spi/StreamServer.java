package org.fourthline.cling.transport.spi;

import java.net.InetAddress;
import org.fourthline.cling.transport.Router;

public interface StreamServer<C extends StreamServerConfiguration> extends Runnable {
  void init(InetAddress paramInetAddress, Router paramRouter) throws InitializationException;
  
  int getPort();
  
  void stop();
  
  C getConfiguration();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\StreamServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */