package org.fourthline.cling.transport;

import java.net.InetAddress;
import java.util.List;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.model.NetworkAddress;
import org.fourthline.cling.model.message.IncomingDatagramMessage;
import org.fourthline.cling.model.message.OutgoingDatagramMessage;
import org.fourthline.cling.model.message.StreamRequestMessage;
import org.fourthline.cling.model.message.StreamResponseMessage;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.transport.spi.InitializationException;
import org.fourthline.cling.transport.spi.UpnpStream;

public interface Router {
  UpnpServiceConfiguration getConfiguration();
  
  ProtocolFactory getProtocolFactory();
  
  boolean enable() throws RouterException;
  
  boolean disable() throws RouterException;
  
  void shutdown() throws RouterException;
  
  boolean isEnabled() throws RouterException;
  
  void handleStartFailure(InitializationException paramInitializationException) throws InitializationException;
  
  List<NetworkAddress> getActiveStreamServers(InetAddress paramInetAddress) throws RouterException;
  
  void received(IncomingDatagramMessage paramIncomingDatagramMessage);
  
  void received(UpnpStream paramUpnpStream);
  
  void send(OutgoingDatagramMessage paramOutgoingDatagramMessage) throws RouterException;
  
  StreamResponseMessage send(StreamRequestMessage paramStreamRequestMessage) throws RouterException;
  
  void broadcast(byte[] paramArrayOfbyte) throws RouterException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\Router.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */