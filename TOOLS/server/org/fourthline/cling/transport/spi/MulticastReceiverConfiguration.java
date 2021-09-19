package org.fourthline.cling.transport.spi;

import java.net.InetAddress;

public interface MulticastReceiverConfiguration {
  InetAddress getGroup();
  
  int getPort();
  
  int getMaxDatagramBytes();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\MulticastReceiverConfiguration.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */