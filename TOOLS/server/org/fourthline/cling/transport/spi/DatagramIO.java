package org.fourthline.cling.transport.spi;

import java.net.DatagramPacket;
import java.net.InetAddress;
import org.fourthline.cling.model.message.OutgoingDatagramMessage;
import org.fourthline.cling.transport.Router;

public interface DatagramIO<C extends DatagramIOConfiguration> extends Runnable {
  void init(InetAddress paramInetAddress, Router paramRouter, DatagramProcessor paramDatagramProcessor) throws InitializationException;
  
  void stop();
  
  C getConfiguration();
  
  void send(OutgoingDatagramMessage paramOutgoingDatagramMessage);
  
  void send(DatagramPacket paramDatagramPacket);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\spi\DatagramIO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */